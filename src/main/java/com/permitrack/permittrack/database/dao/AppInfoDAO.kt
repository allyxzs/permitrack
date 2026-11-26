package com.permitrack.permittrack.database.dao

import com.permitrack.permittrack.database.PermissionDatabase
import com.permitrack.permittrack.model.AppInfo
import java.util.List

interface AppInfoDAO {
    fun getAllApps(): List<AppInfo>
    fun insertApp(app: AppInfo)
    fun updateApp(app: AppInfo)
    fun deleteApp(app: AppInfo)
    fun getAppByPackage(packageName: String): AppInfo?
}