package project.agentworkflowdemonstration.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.agentworkflowdemonstration.model.AgentState
import project.agentworkflowdemonstration.ui.components.CompletionCard
import project.agentworkflowdemonstration.ui.components.GradientButton
import project.agentworkflowdemonstration.ui.components.WorkflowCard
import project.agentworkflowdemonstration.viewmodel.AgentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentScreen(viewModel: AgentViewModel) {
    val agentState by viewModel.agentState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(agentState) {
        if (agentState is AgentState.Processing || agentState is AgentState.Completed) {
            listState.animateScrollToItem(Int.MAX_VALUE)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Title
        Text(
            text = "Agent Workflow Demonstration",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(vertical = 24.dp),
            color = MaterialTheme.colorScheme.primary
        )

        // Input Area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Ask anything...") },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(12.dp))
            GradientButton(
                text = "SEND",
                onClick = {
                    viewModel.processRequest(inputText)
                    inputText = ""
                },
                modifier = Modifier.width(100.dp),
                enabled = agentState is AgentState.Idle || agentState is AgentState.Completed
            )
        }

        // Workflow Visualization
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                when (val state = agentState) {
                    is AgentState.Idle -> {
                        item {
                            EmptyWorkflowState()
                        }
                    }
                    is AgentState.Processing -> {
                        itemsIndexed(state.steps) { index, step ->
                            WorkflowCard(
                                step = step,
                                isVisible = index <= state.currentStepIndex
                            )
                        }
                    }
                    is AgentState.Completed -> {
                        itemsIndexed(state.steps) { _, step ->
                            WorkflowCard(
                                step = step,
                                isVisible = true
                            )
                        }
                        item {
                            CompletionCard(
                                userInput = state.finalItem.userInput,
                                intent = state.finalItem.intent,
                                finalResponse = state.finalItem.finalResponse,
                                confidence = state.finalItem.confidence,
                                timestamp = state.finalItem.timestamp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyWorkflowState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ready to start",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
        Text(
            text = "Enter a request above to see the AI agent in action.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
