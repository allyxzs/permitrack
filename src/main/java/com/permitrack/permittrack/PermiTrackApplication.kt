package com.permitrack.permittrack

import android.app.Application
import com.permitrack.permittrack.database.PermissionDatabase

class PermiTrackApplication : Application() {
    lateinit var database: PermissionDatabase

    override fun onCreate() {
        super.onCreate()
        database = PermissionDatabase.getInstance(this)
    }
}