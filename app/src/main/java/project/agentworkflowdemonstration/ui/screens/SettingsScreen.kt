package project.agentworkflowdemonstration.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import project.agentworkflowdemonstration.viewmodel.AgentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: AgentViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsGroup(title = "App Preferences") {
                SettingsToggleItem(
                    icon = Icons.Default.DarkMode,
                    title = "Dark Mode",
                    description = "Enable modern dark theme",
                    checked = true,
                    onCheckedChange = {}
                )
                SettingsItem(
                    icon = Icons.Default.Speed,
                    title = "Animation Speed",
                    description = "Set workflow animation delay",
                    onClick = {}
                )
                SettingsToggleItem(
                    icon = Icons.Default.VolumeUp,
                    title = "Enable Sounds",
                    description = "Play feedback sounds during tasks",
                    checked = false,
                    onCheckedChange = {}
                )
            }

            SettingsGroup(title = "Maintenance") {
                SettingsItem(
                    icon = Icons.Default.RestartAlt,
                    title = "Reset Demo",
                    description = "Reset agent state and clear temporary data",
                    onClick = { viewModel.resetAgent() }
                )
                SettingsItem(
                    icon = Icons.Default.DeleteForever,
                    title = "Clear All History",
                    description = "Permanently delete all stored conversations",
                    onClick = { viewModel.clearAllHistory() },
                    tint = MaterialTheme.colorScheme.error
                )
            }

            SettingsGroup(title = "Information") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "About Agent Workflow",
                    description = "Version 1.0.0 (Stable)",
                    onClick = {}
                )
                SettingsItem(
                    icon = Icons.Default.Code,
                    title = "Tech Stack",
                    description = "Built with Jetpack Compose & Material 3",
                    onClick = {}
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "© 2026 Agent Workflow Demonstration",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Surface(
        onClick = onClick,
        color = androidx.compose.ui.graphics.Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = tint)
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = tint.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
