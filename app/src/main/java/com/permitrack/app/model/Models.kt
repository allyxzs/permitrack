package com.permitrack.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RiskLevel { LOW, MEDIUM, HIGH, CRITICAL }

data class AppSnapshot(val packageName: String, val label: String, val versionName: String, val versionCode: Long, val requested: List<PermissionSnapshot>, val granted: List<String>)
data class PermissionSnapshot(val name: String, val group: String?, val protection: String, val risk: RiskLevel, val explanation: String, val potentiallyExcessive: Boolean)

@Entity(tableName = "app_records")
data class AppRecord(@PrimaryKey val packageName: String, val label: String, val versionName: String, val versionCode: Long, val lastScanned: Long)

@Entity(primaryKeys = ["packageName", "permissionName"], tableName = "permission_records")
data class PermissionRecord(val packageName: String, val permissionName: String, val groupName: String?, val protection: String, val risk: RiskLevel, val granted: Boolean, val potentiallyExcessive: Boolean, val scanTime: Long)

@Entity(tableName = "permission_changes")
data class PermissionChange(@PrimaryKey(autoGenerate = true) val id: Long = 0, val packageName: String, val permissionName: String, val changeType: String, val detectedAt: Long)
