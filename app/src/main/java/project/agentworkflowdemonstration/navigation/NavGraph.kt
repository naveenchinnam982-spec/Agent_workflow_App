package project.agentworkflowdemonstration.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import project.agentworkflowdemonstration.ui.screens.AgentScreen
import project.agentworkflowdemonstration.ui.screens.HistoryScreen
import project.agentworkflowdemonstration.ui.screens.SettingsScreen
import project.agentworkflowdemonstration.viewmodel.AgentViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Agent : Screen("agent", "Agent", Icons.Default.SmartToy)
    object History : Screen("history", "History", Icons.Default.History)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun NavGraph(navController: NavHostController, viewModel: AgentViewModel) {
    NavHost(navController = navController, startDestination = Screen.Agent.route) {
        composable(Screen.Agent.route) {
            AgentScreen(viewModel)
        }
        composable(Screen.History.route) {
            HistoryScreen(viewModel)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(viewModel)
        }
    }
}

@Composable
fun AgentBottomBar(navController: NavHostController) {
    val items = listOf(Screen.Agent, Screen.History, Screen.Settings)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}
