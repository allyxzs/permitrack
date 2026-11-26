package com.permitrack.app.data

import android.content.Context
import android.content.pm.*
import com.permitrack.app.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class PermissionRepository(private val context: Context, private val db: PermiTrackDatabase) {
    private val pm = context.packageManager
    private val dao = db.dao()

    fun apps() = dao.apps()
    fun permissions(pkg: String) = dao.permissions(pkg)
    fun changes() = dao.changes()
    fun stats() = dao.appCount().combine(dao.grantedCount()) { a, g -> Triple(a, g, 0) }.combine(dao.highRiskCount()) { t, h -> Stats(t.first, t.second, h) }
    data class Stats(val apps: Int, val granted: Int, val highRisk: Int)

    suspend fun refreshInventory() = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA).filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 }
        val newRecords = mutableListOf<AppRecord>()
        val newPerms = mutableListOf<PermissionRecord>()
        val changes = mutableListOf<PermissionChange>()

        val oldApps = dao.allApps().associateBy { it.packageName }
        val oldPerms = dao.allPermissions().groupBy { it.packageName }

        apps.forEach { app ->
            val pkg = app.packageName
            val label = pm.getApplicationLabel(app).toString()
            val info = pm.getPackageInfo(pkg, PackageManager.GET_PERMISSIONS)
            val versionName = info.versionName ?: "?"
            val versionCode = info.longVersionCode

            newRecords += AppRecord(pkg, label, versionName, versionCode, now)

            val requested = info.requestedPermissions?.map { PermissionRiskClassifier.classify(it, protectionLevel(pm, it), label) } ?: emptyList()
            val granted = info.requestedPermissions?.filterIndexed { i, _ -> (info.requestedPermissionsFlags?.getOrNull(i) ?: 0) and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0 } ?: emptyList()

            requested.forEach { p ->
                val isGranted = p.name in granted
                newPerms += PermissionRecord(pkg, p.name, p.group, p.protection, p.risk, isGranted, p.potentiallyExcessive, now)
            }

            // detect changes
            val prev = oldPerms[pkg]?.associateBy { it.permissionName } ?: emptyMap()
            requested.forEach { p ->
                val wasGranted = prev[p.name]?.granted ?: false
                if (wasGranted != (p.name in granted)) {
                    changes += PermissionChange(packageName = pkg, permissionName = p.name, changeType = if (p.name in granted) "GRANTED" else "REVOKED", detectedAt = now)
                }
            }
        }

        dao.clearApps()
        dao.clearPermissions()
        dao.upsertApps(newRecords)
        dao.upsertPermissions(newPerms)
        if (changes.isNotEmpty()) dao.addChanges(changes)
    }

    private fun protectionLevel(pm: PackageManager, name: String): String {
        return try { pm.getPermissionInfo(name, 0).protection.toString() } catch (_: Exception) { "unknown" }
    }

    suspend fun exportReport(uri: android.net.Uri) = withContext(Dispatchers.IO) {
        context.contentResolver.openOutputStream(uri)?.use { out ->
            val w = java.io.BufferedWriter(java.io.OutputStreamWriter(out))
            w.write("# PermiTrack Privacy Report\n")
            w.write("Generated: ${Date()}\n\n")
            dao.allApps().forEach { app ->
                w.write("## ${app.label} (${app.packageName})\n")
                dao.permissions(app.packageName).first().forEach { p ->
                    if (p.granted) w.write("- ${p.permissionName} [${p.risk}]${if (p.potentiallyExcessive) " ⚠️" else ""}\n")
                }
                w.write("\n")
            }
            w.flush()
        }
    }
}
