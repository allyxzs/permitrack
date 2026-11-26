package com.permitrack.permittrack.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "permissions",
    foreignKeys = [
        ForeignKey(
            entity = AppInfo::class,
            parentColumns = ["appPackage"],
            childColumns = ["appPackage"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["appPackage"])]
)
data class PermissionInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "app_package")
    val appPackage: String,
    @ColumnInfo(name = "permission_name")
    val permissionName: String,
    @ColumnInfo(name = "permission_group")
    val permissionGroup: String,
    @ColumnInfo(name = "protection_level")
    val protectionLevel: String,
    @ColumnInfo(name = "risk_level")
    val riskLevel: String = "LOW",
    @ColumnInfo(name = "is_essential")
    val isEssential: Boolean = true,
    @ColumnInfo(name = "date_detected")
    var dateDetected: Long = System.currentTimeMillis()
)