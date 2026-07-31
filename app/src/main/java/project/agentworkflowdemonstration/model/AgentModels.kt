package project.agentworkflowdemonstration.model

import androidx.compose.ui.graphics.vector.ImageVector

enum class AgentStepStatus {
    IDLE, PROCESSING, DONE
}

data class WorkflowStep(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val status: AgentStepStatus = AgentStepStatus.IDLE,
    val detailText: String? = null
)

sealed class AgentState {
    object Idle : AgentState()
    data class Processing(val currentStepIndex: Int, val steps: List<WorkflowStep>) : AgentState()
    data class Completed(val steps: List<WorkflowStep>, val finalItem: HistoryItem) : AgentState()
}

enum class AgentIntent(val displayName: String, val confidence: Int) {
    WEATHER("Weather Query", 97),
    ALARM("Alarm Creation", 95),
    CALCULATION("Calculation", 99),
    REMINDER("Reminder", 94),
    MUSIC("Music Playback", 96),
    OPEN_APP("Open Application", 98),
    ENTERTAINMENT("Entertainment", 92),
    UNKNOWN("Unknown", 45)
}
