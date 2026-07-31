package project.agentworkflowdemonstration.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import project.agentworkflowdemonstration.data.repository.HistoryRepository
import project.agentworkflowdemonstration.model.*

class AgentViewModel(private val repository: HistoryRepository) : ViewModel() {

    private val _agentState = MutableStateFlow<AgentState>(AgentState.Idle)
    val agentState: StateFlow<AgentState> = _agentState.asStateFlow()

    private val _history = repository.allHistory.stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )
    val history = _history

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredHistory = combine(_history, _searchQuery) { history, query ->
        if (query.isEmpty()) history
        else history.filter { 
            it.userInput.contains(query, ignoreCase = true) || 
            it.intent.contains(query, ignoreCase = true) 
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteHistoryItem(item: HistoryItem) {
        viewModelScope.launch {
            repository.deleteHistory(item)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.deleteAllHistory()
        }
    }

    fun processRequest(input: String) {
        if (input.isBlank()) return
        
        viewModelScope.launch {
            val intent = detectIntent(input)
            val plan = generatePlan(intent)
            
            val steps = mutableListOf(
                WorkflowStep("Receive Input", "User request received successfully.", Icons.Default.SmartToy, AgentStepStatus.DONE),
                WorkflowStep("Intent Detection", "Analyzing user intent...", Icons.Default.Psychology),
                WorkflowStep("Reasoning & Planning", "Formulating execution steps...", Icons.Default.List),
                WorkflowStep("Action Execution", "Executing simulated action...", Icons.Default.Settings),
                WorkflowStep("Final Response", "Generating output...", Icons.Default.CheckCircle)
            )

            _agentState.value = AgentState.Processing(0, steps.toList())
            delay(800)

            // Step 2: Intent Detection
            steps[1] = steps[1].copy(
                status = AgentStepStatus.DONE, 
                detailText = "Intent: ${intent.displayName}\nConfidence: ${intent.confidence}%"
            )
            _agentState.value = AgentState.Processing(1, steps.toList())
            delay(1200)

            // Step 3: Planning
            steps[2] = steps[2].copy(
                status = AgentStepStatus.DONE,
                detailText = plan.joinToString("\n") { "• $it" }
            )
            _agentState.value = AgentState.Processing(2, steps.toList())
            delay(1500)

            // Step 4: Execution
            steps[3] = steps[3].copy(status = AgentStepStatus.PROCESSING)
            _agentState.value = AgentState.Processing(3, steps.toList())
            delay(2000)
            steps[3] = steps[3].copy(status = AgentStepStatus.DONE, detailText = "Action completed successfully.")
            _agentState.value = AgentState.Processing(3, steps.toList())
            delay(800)

            // Step 5: Final Response
            val response = generateResponse(intent, input)
            steps[4] = steps[4].copy(status = AgentStepStatus.DONE, detailText = response)
            
            val historyItem = HistoryItem(
                userInput = input,
                intent = intent.displayName,
                confidence = intent.confidence,
                planSteps = plan.joinToString("\n"),
                executedAction = "Simulated ${intent.displayName} execution",
                finalResponse = response
            )
            
            repository.insertHistory(historyItem)
            _agentState.value = AgentState.Completed(steps.toList(), historyItem)
        }
    }

    fun resetAgent() {
        _agentState.value = AgentState.Idle
    }

    private fun detectIntent(input: String): AgentIntent {
        val low = input.lowercase()
        return when {
            low.contains("weather") -> AgentIntent.WEATHER
            low.contains("alarm") -> AgentIntent.ALARM
            low.contains("calculate") || low.contains("calc") || low.contains("x") || low.contains("+") -> AgentIntent.CALCULATION
            low.contains("remind") -> AgentIntent.REMINDER
            low.contains("music") -> AgentIntent.MUSIC
            low.contains("youtube") || low.contains("open") -> AgentIntent.OPEN_APP
            low.contains("joke") -> AgentIntent.ENTERTAINMENT
            else -> AgentIntent.UNKNOWN
        }
    }

    private fun generatePlan(intent: AgentIntent): List<String> {
        return when (intent) {
            AgentIntent.WEATHER -> listOf("Access weather API", "Extract location", "Fetch current temperature", "Format forecast data")
            AgentIntent.ALARM -> listOf("Validate time format", "Check system permissions", "Schedule alarm event", "Set notification channel")
            AgentIntent.CALCULATION -> listOf("Parse mathematical expression", "Identify operators", "Perform arithmetic", "Round final result")
            AgentIntent.REMINDER -> listOf("Extract reminder text", "Determine due date", "Persist in database", "Setup trigger service")
            AgentIntent.MUSIC -> listOf("Search music library", "Select high-quality stream", "Buffer audio data", "Initialize playback engine")
            AgentIntent.OPEN_APP -> listOf("Identify package name", "Check app installation", "Construct launch intent", "Execute start activity")
            AgentIntent.ENTERTAINMENT -> listOf("Select random joke", "Check content filters", "Retrieve punchline", "Format for display")
            AgentIntent.UNKNOWN -> listOf("Analyze semantic structure", "Calculate intent probabilities", "Check fallback services", "Request clarification")
        }
    }

    private fun generateResponse(intent: AgentIntent, input: String): String {
        return when (intent) {
            AgentIntent.WEATHER -> "Today's weather is sunny with a temperature of 24°C."
            AgentIntent.ALARM -> "Alarm has been set for tomorrow at 7:00 AM."
            AgentIntent.CALCULATION -> "The result of your calculation is 450."
            AgentIntent.REMINDER -> "Reminder created successfully."
            AgentIntent.MUSIC -> "Now playing your favorite relaxing music."
            AgentIntent.OPEN_APP -> "Opening the requested application..."
            AgentIntent.ENTERTAINMENT -> "Why don't programmers like nature? Because it has too many bugs."
            AgentIntent.UNKNOWN -> "I'm not sure how to help with that yet, but I've logged the request."
        }
    }
}
