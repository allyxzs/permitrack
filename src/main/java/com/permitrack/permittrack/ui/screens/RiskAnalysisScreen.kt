package com.permitrack.permittrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressBar
import androidx.compose.material3.Skeleton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.permitrack.permittrack.MainViewModel
import com.permitrack.permittrack.model.AppInfo
import com.permitrack.permittrack.model.PermissionInfo
import com.permitrack.permittrack.model.RiskLevel
import com.permitrack.permittrack.ui.theme.PermiTrackTheme
import com.permitrack.permittrack.viewmodel.MainViewModel

@Composable
fun RiskAnalysisScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel()
    val apps = viewModel.appState.value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterStart
       ) {
            Text(
                text = "Análise de Riscos",
                style = MaterialTheme.typography.h2,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            if (apps.isEmpty()) {
                Text(
                    text = "Nenhum dado disponível. Escaneie os aplicativos primeiro.",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 32.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            } else {
                RiskOverviewStats(apps)
                Spacer(Modifier.height(24.dp))
                DetailedRiskAnalysis(apps)
            }
        }
    }
}

@Composable
fun RiskOverviewStats(apps: List<AppInfo>) {
    val totalPermissions = apps.sumOf { getPermissionsCount(it.appPackage) }
    val highRisk = apps.count { determineAppRisk(it.appPackage) == "HIGH" }
    val criticalApps = apps.count { determineAppRisk(it.appPackage) == "CRITICAL" }

    // Stats grid
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        RiskStatCard(
            title = "Total de Permissões",
            value = totalPermissions.toString(),
            icon = Icons.Default.Badge,
            color = MaterialTheme.colorScheme.primary
        )
        RiskStatCard(
            title = "Apps de Alto Risco",
            value = highRisk.toString(),
            icon = Icons.Default.Error,
            color = Color(0xFFF44336)
        )
        RiskStatCard(
            title = "Apps Críticos",
            value = criticalApps.toString(),
            icon = Icons.Default.Close,
            color = Color(0xFFB71C1C)
        )
    }
}

@Composable
fun RiskStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.Bitmap,
    color: Color
) {
    Card(
        modifier = Modifier.width(140.dp).height(80.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                contentScale = IconContentScale.Fit,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun DetailedRiskAnalysis(apps: List<AppInfo>) {
    Text(
        text = "Análise Detalhada",
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.SemiBold
    )

    Spacer(Modifier.height(16.dp))

    if (apps.isEmpty()) {
        Text("Nenhum aplicativo para analisar", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(apps) { app ->
                AppRiskCard(app)
            }
        }
    }
}

@Composable
fun AppRiskCard(app: AppInfo) {
    val appRisk = determineAppRisk(app.appPackage)
    val riskColor = when (appRisk) {
        "LOW" -> Color(0xFF4CAF50)
        "MEDIUM" -> Color(0xFFFF9800)
        "HIGH" -> Color(0xFFF44336)
        "CRITICAL" -> Color(0xFFB71C1C)
        else -> Color.GRAY
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        elevation = 2.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Start,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = truncateText(app.appName, 25),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = appRisk,
                    style = MaterialTheme.typography.body2,
                    color = riskColor,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Versão: ${app.versionName}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )

            Spacer(Modifier.height(4.dp))

            // Permission count
            Text(
                text = "${getPermissionsCount(app.appPackage)} permissões detectadas",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

fun getPermissionsCount(packageName: String): Int {
    // Placeholder - would query database
    return 5
}

fun truncateText(text: String, maxLength: Int): String {
    return if (text.length > maxLength) {
        text.substring(0, maxLength - 3) + "..."
    } else {
        text
    }
}

fun determineAppRisk(packageName: String): String {
    return "MEDIUM" // placeholder
}