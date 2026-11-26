package com.permitrack.permittrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.permitrack.permittrack.MainViewModel
import com.permitrack.permittrack.model.AppInfo
import com.permitrack.permittrack.model.PermissionInfo
import com.permitrack.permittrack.ui.theme.PermiTrackTheme
import com.permitrack.permittrack.viewmodel.MainViewModel

@Composable
fun PrivacyReportScreen(navController: NavController) {
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
                text = "Relatório de Privacidade",
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
                PrivacyOverviewCard(apps)
                Spacer(Modifier.height(24.dp))
                PrivacyDetailsList(apps)
            }

            Spacer(Modifier.height(24.dp))

            // Generate report button
            OutlinedTextButton(
                onClick = { /* generate report */ }
            ) {
                Text(
                    text = "Gerar Relatório Completo",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Composable
fun PrivacyOverviewCard(apps: List<AppInfo>) {
    Card(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Start,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Resumo de Privacidade",
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            val totalApps = apps.size
            val highRiskApps = apps.count { determineAppRisk(it.appPackage) == "HIGH" }
            val criticalApps = apps.count { determineAppRisk(it.appPackage) == "CRITICAL" }

            // Stats row
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PrivacyStatItem("Aplicativos", totalApps.toString(), MaterialTheme.colorScheme.primary)
                PrivacyStatItem("Alto Risco", highRiskApps.toString(), Color(0xFFF44336))
                PrivacyStatItem("Críticos", criticalApps.toString(), Color(0xFFB71C1C))
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Seus dados de privacidade estão em risco. ${highRiskApps} aplicativos têm permissões sensíveis.",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun PrivacyStatItem(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(
        modifier = Modifier.width(80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterStart
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun PrivacyDetailsList(apps: List<AppInfo>) {
    Text(
        text = "Apps com Riscos de Privacidade",
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.SemiBold
    )

    Spacer(Modifier.height(16.dp))

    if (apps.isEmpty()) {
        Text("Nenhum aplicativo para analisar", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(apps) { app ->
                PrivacyDetailItem(app)
            }
        }
    }
}

@Composable
fun PrivacyDetailItem(app: AppInfo) {
    val appRisk = determineAppRisk(app.appPackage)

    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
        elevation = 1.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Start,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = truncateText(app.appName, 25),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Risco: $appRisk",
                    style = MaterialTheme.typography.body2,
                    color = if (appRisk == "CRITICAL") Color(0xFFB71C1C) else Color(0xFFFF9800)
                )

                // Would show count of sensitive permissions
                Text(
                    text = "${getPermissionsCount(app.appPackage)} permissões",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Permission type indicators
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(getPermissionTypes(app.appPackage)) { permissionType ->
                    Text(
                        text = permissionType,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier
                            .background(Color(0x33000000), RoundedCornerShape(8.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

fun getPermissionTypes(packageName: String): List<String> {
    // Placeholder
    return listOf("Câmera", "Localização", "Microfone")
}

fun truncateText(text: String, maxLength: Int): String {
    return if (text.length > maxLength) {
        text.substring(0, maxLength - 3) + "..."
    } else {
        text
    }
}

fun determineAppRisk(packageName: String): String {
    return "HIGH" // placeholder
}