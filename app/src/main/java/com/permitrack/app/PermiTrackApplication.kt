package com.permitrack.app

import android.app.Application
import androidx.room.Room
import com.permitrack.app.data.*

class PermiTrackApplication : Application() {
    lateinit var repository: PermissionRepository
    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(this, PermiTrackDatabase::class.java, "permitrack.db").build()
        repository = PermissionRepository(this, db)
    }
}
