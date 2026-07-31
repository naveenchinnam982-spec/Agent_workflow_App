package project.agentworkflowdemonstration.data.repository

import kotlinx.coroutines.flow.Flow
import project.agentworkflowdemonstration.data.local.HistoryDao
import project.agentworkflowdemonstration.model.HistoryItem

class HistoryRepository(private val historyDao: HistoryDao) {
    val allHistory: Flow<List<HistoryItem>> = historyDao.getAllHistory()

    fun searchHistory(query: String): Flow<List<HistoryItem>> {
        return historyDao.searchHistory(query)
    }

    suspend fun insertHistory(item: HistoryItem) {
        historyDao.insertHistory(item)
    }

    suspend fun deleteHistory(item: HistoryItem) {
        historyDao.deleteHistory(item)
    }

    suspend fun deleteAllHistory() {
        historyDao.deleteAllHistory()
    }
}
