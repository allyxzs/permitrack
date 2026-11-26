package com.permitrack.permittrack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.flow
import com.permitrack.permittrack.model.AppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.permitrack.permittrack.database.PermissionDatabase

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = PermissionDatabase.getInstance(application)
    private val _appState = MutableStateFlow<List<AppInfo>>(emptyList())
    val appState: StateFlow<List<AppInfo>> = _appState

    init {
        val appDao = database.appInfoDAO()
        val apps = appDao.getAllApps()
        _appState.value = apps
    }
}