package to.msn.wings.carmaintenancerecord.data.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    fun observeIsDarkMode(): Flow<Boolean>
    suspend fun setDarkMode(isDarkMode: Boolean)
}
