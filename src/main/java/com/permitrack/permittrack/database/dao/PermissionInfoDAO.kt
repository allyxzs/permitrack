package com.permitrack.permittrack.database.dao

import com.permitrack.permittrack.model.PermissionInfo
import com.permitrack.permittrack.database.PermissionDatabase

interface PermissionInfoDAO {
    fun getPermissionsForApp(packageName: String): List<PermissionInfo>
    fun getAllPermissions(): List<PermissionInfo>
    fun insertPermission(permission: PermissionInfo)
    fun updatePermission(permission: PermissionInfo)
    fun deletePermission(permission: PermissionInfo)
}