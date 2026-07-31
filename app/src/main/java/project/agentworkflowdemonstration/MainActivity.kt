package project.agentworkflowdemonstration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import project.agentworkflowdemonstration.data.local.HistoryDatabase
import project.agentworkflowdemonstration.data.repository.HistoryRepository
import project.agentworkflowdemonstration.navigation.AgentBottomBar
import project.agentworkflowdemonstration.navigation.NavGraph
import project.agentworkflowdemonstration.ui.theme.AgentWorkflowDemonstrationTheme
import project.agentworkflowdemonstration.viewmodel.AgentViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            val database = remember { HistoryDatabase.getDatabase(context) }
            val repository = remember { HistoryRepository(database.historyDao()) }
            val factory = remember { AgentViewModelFactory(repository) }
            val viewModel: project.agentworkflowdemonstration.viewmodel.AgentViewModel = viewModel(factory = factory)
            val navController = rememberNavController()

            AgentWorkflowDemonstrationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { AgentBottomBar(navController) }
                ) { innerPadding ->
                    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(navController = navController, viewModel = viewModel)
                    }
                }
            }
        }
    }
}
