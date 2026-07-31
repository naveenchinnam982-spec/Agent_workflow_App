package project.agentworkflowdemonstration.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_items")
data class HistoryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userInput: String,
    val intent: String,
    val confidence: Int,
    val planSteps: String, // Stored as a newline separated string
    val executedAction: String,
    val finalResponse: String,
    val timestamp: Long = System.currentTimeMillis()
)
