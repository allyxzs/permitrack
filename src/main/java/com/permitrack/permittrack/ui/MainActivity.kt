package com.permitrack.permittrack.ui

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.permitrack.permittrack.PermiTrackApplication
import com.permitrack.permittrack.ui.navigation.PermiTrackNavHost
import com.permitrack.permittrack.ui.theme.PermiTrackTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle permissions request result
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermiTrackTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PermiTrackNavHost()
                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionsToCheck = arrayOf(
            android.Manifest.permission.QUERY_ALL_PACKAGES,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val missingPermissions = permissionsToCheck.filter {
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }
        return missingPermissions.isEmpty()
    }

    private fun requestPermissions() {
        val permissionsToRequest = arrayOf(
            android.Manifest.permission.QUERY_ALL_PACKAGES,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        requestPermissionsLauncher.launch(permissionsToRequest)
    }
}