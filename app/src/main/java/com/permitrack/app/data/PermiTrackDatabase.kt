package com.permitrack.app.data

import androidx.room.*
import com.permitrack.app.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PermiTrackDao {
    @Query("SELECT * FROM app_records ORDER BY label COLLATE NOCASE") fun apps(): Flow<List<AppRecord>>
    @Query("SELECT * FROM permission_records WHERE packageName = :packageName ORDER BY risk DESC, permissionName") fun permissions(packageName: String): Flow<List<PermissionRecord>>
    @Query("SELECT * FROM permission_changes ORDER BY detectedAt DESC") fun changes(): Flow<List<PermissionChange>>
    @Query("SELECT COUNT(*) FROM app_records") fun appCount(): Flow<Int>
    @Query("SELECT COUNT(*) FROM permission_records WHERE granted = 1") fun grantedCount(): Flow<Int>
    @Query("SELECT COUNT(*) FROM permission_records WHERE risk IN ('HIGH','CRITICAL') AND granted = 1") fun highRiskCount(): Flow<Int>
    @Query("SELECT * FROM permission_records") suspend fun allPermissions(): List<PermissionRecord>
    @Query("SELECT * FROM app_records") suspend fun allApps(): List<AppRecord>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertApps(items: List<AppRecord>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPermissions(items: List<PermissionRecord>)
    @Insert suspend fun addChanges(items: List<PermissionChange>)
    @Query("DELETE FROM app_records") suspend fun clearApps()
    @Query("DELETE FROM permission_records") suspend fun clearPermissions()
}

@Database(entities = [AppRecord::class, PermissionRecord::class, PermissionChange::class], version = 1, exportSchema = false)
@TypeConverters(RiskConverters::class)
abstract class PermiTrackDatabase : RoomDatabase() {
    abstract fun dao(): PermiTrackDao
    companion object { fun create(context: android.content.Context) = Room.databaseBuilder(context, PermiTrackDatabase::class.java, "permitrack.db").build() }
}
class RiskConverters { @TypeConverter fun fromRisk(v: RiskLevel) = v.name; @TypeConverter fun toRisk(v: String) = RiskLevel.valueOf(v) }
