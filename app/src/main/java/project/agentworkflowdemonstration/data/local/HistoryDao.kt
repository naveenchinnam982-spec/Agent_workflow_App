package project.agentworkflowdemonstration.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import project.agentworkflowdemonstration.model.HistoryItem

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_items ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<HistoryItem>>

    @Query("SELECT * FROM history_items WHERE userInput LIKE '%' || :query || '%' OR intent LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchHistory(query: String): Flow<List<HistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(item: HistoryItem)

    @Delete
    suspend fun deleteHistory(item: HistoryItem)

    @Query("DELETE FROM history_items")
    suspend fun deleteAllHistory()
}
