package com.permitrack.permittrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.permitrack.permittrack.ui.screens.AppListScreen
import com.permitrack.permittrack.ui.screens.MainScreen
import com.permitrack.permittrack.ui.screens.PermissionDetailScreen
import com.permitrack.permittrack.ui.screens.PrivacyReportScreen
import com.permitrack.permittrack.ui.screens.RiskAnalysisScreen

@Composable
fun PermiTrackNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable("apps") { AppListScreen(navController) }
        composable("permission/{packageName}") { backStackEntry ->
            PermissionDetailScreen(
                navController,
                backStackEntry.arguments?.getString("packageName") ?: ""
            )
        }
        composable("risk") { RiskAnalysisScreen(navController) }
        composable("report") { PrivacyReportScreen(navController) }
    }
}