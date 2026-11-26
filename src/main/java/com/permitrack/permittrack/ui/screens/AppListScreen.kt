package com.permitrack.permittrack.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.permitrack.permittrack.MainActivity
import com.permitrack.permittrack.model.AppInfo
import com.permitrack.permittrack.model.PermissionInfo
import com.permitrack.permittrack.model.RiskLevel
import com.permitrack.permittrack.ui.theme.PermiTrackTheme
import com.permitrack.permittrack.viewmodel.MainViewModel

@Composable
fun AppListScreen(navController: NavController) {
    val viewModel: MainViewModel = viewModel()
    val apps = viewModel.appState.value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            // Header with back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aplicativos",
                    style = MaterialTheme.typography.h2,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Voltar",
                    modifier = Modifier.clickable { navController.popBackStack() },
                    style = MaterialTheme.typography.body1
                )
            }

            Spacer(Modifier.height(16.dp))

            if (apps.isEmpty()) {
                Text(
                    text = "Nenhum aplicativo encontrado. Execute uma varredura primeiro.",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 32.dp)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(apps) { app ->
                        AppListCard(app, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun AppListCard(app: AppInfo, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("permission/${app.appPackage}") },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = truncateText(app.appName, 25),
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Versão: ${app.versionName} (${app.versionCode})",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Risk badge
                RiskBadge(riskLevel = RiskLevel.HIGH)
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Última verificação: ${formatDate(app.lastScanned)}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun RiskBadge(riskLevel: RiskLevel) {
    val (color, text) = when (riskLevel) {
        RiskLevel.LOW -> Pair(Color(0xFF4CAF50), "Baixo")
        RiskLevel.MEDIUM -> Pair(Color(0xFFFF9800), "Médio")
        RiskLevel.HIGH -> Pair(Color(0xFFF44336), "Alto")
        RiskLevel.CRITICAL -> Pair(Color(0xFFB71C1C), "Crítico")
    }

    Surface(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = color,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold
        )
    }
}

fun truncateText(text: String, maxLength: Int): String {
    return if (text.length > maxLength) {
        text.substring(0, maxLength - 3) + "..."
    } else {
        text
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}