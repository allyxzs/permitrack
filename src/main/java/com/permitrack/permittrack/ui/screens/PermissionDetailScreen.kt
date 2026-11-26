package com.permitrack.permittrack.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Chip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.permitrack.permittrack.MainViewModel
import com.permitrack.permittrack.model.AppInfo
import com.permitrack.permittrack.model.PermissionInfo
import com.permitrack.permittrack.model.RiskLevel
import com.permitrack.permittrack.ui.theme.PermiTrackTheme
import java.util.Locale

@Composable
fun PermissionDetailScreen(
    navController: NavController,
    packageName: String
) {
    val viewModel: MainViewModel = viewModel()
    val app = viewModel.appState.value.find { it.appPackage == packageName } ?: run {
        Text("Aplicativo não encontrado", style = MaterialTheme.typography.body1)
        return@Composable
    }

    val appPermissions = getPermissionsForApp(packageName)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterStart
        ) {
            // App header info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = app.appName,
                        style = MaterialTheme.typography.h2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${app.versionName} (${app.versionCode})",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                RiskBadge(riskLevel = determineAppRisk(packageName))
            }

            Spacer(Modifier.height(24.dp))

            // Permissions list header
            Text(
                text = "Permissões concedidas:",
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(8.dp))

            if (appPermissions.isEmpty()) {
                Text(
                    text = "Nenhuma permissão detectada",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 32.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(appPermissions) { permission ->
                        PermissionChip(permission)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Privacy info
            PrivacyInfoSection(packageName)
        }
    }
}

@Composable
fun PermissionChip(permission: PermissionInfo) {
    val riskColor = permissionRiskColor(permission.riskLevel)
    val isDangerous = permission.protectionLevel == "dangerous"

    Surface(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = permissionRiskColorOverlay(permission.riskLevel),
        elevation = 1.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = permission.permissionName,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                SmallInfoText(permission.permissionGroup)
            }

            Text(
                text = permissionRiskLabel(permission.riskLevel),
                style = MaterialTheme.typography.body2,
                color = riskColor,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun SmallInfoText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.body2,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        modifier = Modifier.padding(top = 2.dp)
    )
}

fun permissionRiskLabel(risk: String): String {
    return when (risk) {
        "LOW" -> "Baixo risco"
        "MEDIUM" -> "Risco médio"
        "HIGH" -> "Alto risco"
        "CRITICAL" -> "Crítico"
        else -> risk
    }
}

fun permissionRiskColor(risk: String): Color {
    return when (risk) {
        "LOW" -> Color(0xFF4CAF50)
        "MEDIUM" -> Color(0xFFFF9800)
        "HIGH" -> Color(0xFFF44336)
        "CRITICAL" -> Color(0xFFB71C1C)
        else -> Color.GRAY
    }
}

fun permissionRiskColorOverlay(risk: String): androidx.compose.ui.graphics.Color {
    return when (risk) {
        "LOW" -> androidx.compose.ui.graphics.Color.Transparent
        "MEDIUM" -> androidx.compose.ui.graphics.Color(0x33FFFF9800)
        "HIGH" -> androidx.compose.ui.graphics.Color(0x33F44336)
        "CRITICAL" -> androidx.compose.ui.graphics.Color(0x33B71C1C)
        else -> androidx.compose.ui.graphics.Color.Transparent
    }
}

fun determineAppRisk(packageName: String): String {
    // Query database to determine overall risk
    return "MEDIUM" // placeholder
}

fun getPermissionsForApp(packageName: String): List<PermissionInfo> {
    // Placeholder - would query database
    return emptyList()
}

fun PrivacyInfoSection(packageName: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Informações de Privacidade",
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Este aplicativo possui acesso a dados sensíveis que podem ser usados para rastreamento.",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            // Would show specific privacy risks based on permissions
            Spacer(Modifier.height(8.dp))

            OutlinedTextButton(
                onClick = { /* share privacy report */ }
            ) {
                Text(
                    text = "Gerar Relatório de Privacidade",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Composable
fun OutlinedTextButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.OutlinedButton(onClick = onClick) {
        content()
    }
}