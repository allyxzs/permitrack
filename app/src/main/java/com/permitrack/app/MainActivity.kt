package com.permitrack.app

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.lifecycleScope
import com.permitrack.app.data.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var pendingReportUri: Uri? = null
    private val createReport = registerForActivityResult(ActivityResultContracts.CreateDocument("text/markdown")) { uri -> if (uri != null) (application as PermiTrackApplication).repository.let { r -> lifecycleScope.launch { r.exportReport(uri) } } }
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { PermiTrackUi((application as PermiTrackApplication).repository, { createReport.launch("permitrack-report.md") }) } }
}

class MainVm(private val repo: PermissionRepository) : ViewModel() {
    val apps = repo.apps().stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000), emptyList())
    val stats = repo.stats().stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000), PermissionRepository.Stats(0,0,0))
    var query by mutableStateOf(""); var refreshing by mutableStateOf(false); var error by mutableStateOf<String?>(null)
    fun refresh() { viewModelScope.launch { refreshing = true; error = null; runCatching { repo.refreshInventory() }.onFailure { error = it.message ?: "Não foi possível atualizar" }; refreshing = false } }
}

@Composable fun PermiTrackUi(repo: PermissionRepository, onExport: () -> Unit) {
    val vm: MainVm = viewModel(factory = object : ViewModelProvider.Factory { override fun <T : ViewModel> create(c: Class<T>) = MainVm(repo) as T })
    val apps by vm.apps.collectAsStateWithLifecycle(); val stats by vm.stats.collectAsStateWithLifecycle()
    val filtered = apps.filter { it.label.contains(vm.query, true) || it.packageName.contains(vm.query, true) }
    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF365A7A))) {
        Scaffold(topBar = { TopAppBar(title = { Text("PermiTrack 0.2.0") }, actions = { IconButton({ vm.refresh() }) { Icon(Icons.Default.Refresh, "Atualizar") }; IconButton(onExport) { Icon(Icons.Default.FileDownload, "Exportar relatório") } }) }) { pad ->
            Column(Modifier.padding(pad).padding(16.dp)) {
                OutlinedTextField(vm.query, { vm.query = it }, Modifier.fillMaxWidth(), label = { Text("Pesquisar aplicativos") }, leadingIcon = { Icon(Icons.Default.Search, null) })
                Spacer(Modifier.height(12.dp)); Text("${stats.apps} apps · ${stats.granted} permissões concedidas · ${stats.highRisk} de alto risco", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                if (vm.error != null) Text(vm.error!!, color = MaterialTheme.colorScheme.error)
                if (vm.refreshing) LinearProgressIndicator(Modifier.fillMaxWidth())
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(filtered) { app -> AppCard(app, repo) } }
            }
        }
    }
}

@Composable private fun AppCard(app: AppRecord, repo: PermissionRepository) {
    var expanded by remember { mutableStateOf(false) }; val perms by repo.permissions(app.packageName).collectAsStateWithLifecycle(emptyList())
    Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(14.dp)) { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Column(Modifier.weight(1f)) { Text(app.label, style = MaterialTheme.typography.titleMedium); Text(app.packageName, style = MaterialTheme.typography.bodySmall) }; Text("${perms.count { it.granted }} concedidas") }; TextButton({ expanded = !expanded }) { Text(if (expanded) "Ocultar permissões" else "Ver permissões") }; if (expanded) perms.filter { it.granted }.forEach { p -> Text("${p.risk}: ${p.permissionName}${if (p.potentiallyExcessive) " · potencialmente excessiva" else ""}", style = MaterialTheme.typography.bodySmall) } } }
}
