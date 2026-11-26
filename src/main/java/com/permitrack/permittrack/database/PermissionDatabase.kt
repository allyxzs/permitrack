package com.permitrack.permittrack.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.permitrack.permittrack.database.dao.AppInfoDAO
import com.permitrack.permittrack.database.dao.PermissionInfoDAO
import com.permitrack.permittrack.model.AppInfo
import com.permitrack.permittrack.model.PermissionInfo

@Database(entities = [AppInfo::class, PermissionInfo::class], version = 1, exportSchema = false)
abstract class PermissionDatabase : RoomDatabase() {

    abstract fun appInfoDAO(): AppInfoDAO
    abstract fun permissionInfoDAO(): PermissionInfoDAO

    companion object {
        @Volatile
        private var INSTANCE: PermissionDatabase? = null

        fun getInstance(context: Context): PermissionDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    PermissionDatabase::class.java,
                    "permittrack-db"
                ).build()
                INSTANCE = database
                database
            }
        }

        fun clearInstance() {
            INSTANCE = null
        }
    }
}