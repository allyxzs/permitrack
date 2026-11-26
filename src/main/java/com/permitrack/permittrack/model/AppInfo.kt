package com.permitrack.permittrack.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apps")
data class AppInfo(
    @PrimaryKey
    val appPackage: String,
    @ColumnInfo(name = "app_name")
    val appName: String,
    @ColumnInfo(name = "version_code")
    val versionCode: Int,
    @ColumnInfo(name = "version_name")
    val versionName: String,
    @ColumnInfo(name = "last_scanned")
    var lastScanned: Long = System.currentTimeMillis()
)