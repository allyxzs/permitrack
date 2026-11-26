package com.permitrack.permittrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.constrainAs
import androidx.compose.foundation.layout.guide
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberPaddingEvent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.NavigationIcon
import androidx.compose.ui.Offset
import androidx.compose.ui.Padding
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.isFocusedAndKeyboardVisible
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.keyevent
import androidx.compose.ui.semantics.isFocusable
import androidx.compose.ui.semantics.clickable
import androidx.compose.ui.semantics.tabIndex
import androidx.compose.ui.snaphelper.autoScroll
import androidx.compose.ui.snaphelper.snap
import androidx.compose.ui.snaphelper.snappingContainer
import androidx.compose.ui.spinning
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.filter
import androidx.compose.ui.text.input.onTextInput
import androidx.compose.ui.text.input.textClient
import androidx.compose.ui.text.input.value
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.offset
import androidx.compose.ui.layout.preferredSize
import androidx.compose.ui.layout.size
import androidx.compose.ui.layout.width
import androidx.compose.ui.text.layout.WrapColumnWrap
import androidx.compose.ui.unit.px
import androidx.compose.ui.unit.ps
import com.permitrack.permittrack.MainViewModel
import com.permitrack.permittrack.model.AppInfo
import com.permitrack.permittrack.model.PermissionInfo
import com.permitrack.permittrack.model.RiskLevel
import com.permitrack.permittrack.ui.theme.PermiTrackTheme
import com.permitrack.permittrack.viewmodel.MainViewModel
import kotlin.run.catching

@Composable
fun MainScreen(navController: androidx.navigation.NavController) {
    val viewModel: MainViewModel = viewModel()
    val apps = viewModel.appState.value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.Horizontal.Start
        ) {
            // Header
            Text(
                text = "PermiTrack",
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Stats overview row
            Row(
                modifier = Modifier.fillMaxWidth(),
                arrangement = Arrangement.SpaceEvenly,
                verticalAlignment = androidx.compose.ui.Alignment.Vertical.Center
            ) {
                statsCard("Apps", apps.size.toString(), Icons.Default.AirplaneMode)
                statsCard("Permissões", totalPermissions(apps).toString(), Icons.Default.Badge)
                statsCard("Riscos", countRisks(apps).toString(), Icons.Default.Error)
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Aplicativos instalados:",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // App list
            if (apps.isEmpty()) {
                Text(
                    text = "Nenhum aplicativo encontrado.",
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(top = 32.dp)
                )
            } else {
                AppListComponent(apps, navController)
            }
        }
    }
}

@Composable
fun statsCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.Bitmap
) {
    Surface(
        modifier = Modifier.size(120.dp).background(MaterialTheme.colorScheme.surface).clip(RoundedCornerShape(16.dp)),
        elevation = 4.dp
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
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun totalPermissions(apps: List<AppInfo>): Int {
    // This would need the PermissionInfoDAO - placeholder for now
    return apps.size * 5 // Approximate
}

@Composable
fun countRisks(apps: List<AppInfo>): Int {
    // Placeholder implementation
    return apps.size
}

@Composable
fun AppListComponent(
    apps: List<AppInfo>,
    navController: androidx.navigation.NavController
) {
    items(apps) { app ->
        Surface(
            modifier = Modifier
                .fillWidth()
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(12.dp)),
            elevation = 2.dp,
            onClick = { /* navigate to app details */ }
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = truncateAppName(app.appName, 30),
                        style = MaterialTheme.typography.body1,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    // Risk indicator badge
                    val riskColor = determineRiskColor(app.appPackage)
                    Text(
                        text = riskIcon(RiskLevel.HIGH), // placeholder
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(start = 8.dp),
                        contentDescription = null,
                        color = riskColor
                    )
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Versão: ${app.versionName}",
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "${app.versionCode}",
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

fun determineRiskColor(appPackage: String): androidx.compose.ui.graphics.Color {
    // Placeholder - would query database
    return MaterialTheme.colorScheme.secondary
}

fun riskIcon(risk: RiskLevel): ImageVector {
    return when (risk) {
        RiskLevel.LOW -> Icons.Default.CheckCircle
        RiskLevel.MEDIUM -> Icons.Default.Warning
        RiskLevel.HIGH -> Icons.Default.Error
        RiskLevel.CRITICAL -> Icons.Default.Close
    }
}

fun truncateAppName(name: String, maxLength: Int): String {
    return if (name.length > maxLength) {
        name.substring(0, maxLength - 3) + "..."
    } else {
        name
    }
}