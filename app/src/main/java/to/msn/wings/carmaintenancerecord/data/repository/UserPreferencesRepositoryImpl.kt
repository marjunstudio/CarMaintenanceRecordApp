package to.msn.wings.carmaintenancerecord.data.repository

import kotlinx.coroutines.flow.Flow
import to.msn.wings.carmaintenancerecord.data.local.datastore.UserPreferencesDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: UserPreferencesDataStore
) : UserPreferencesRepository {

    override fun observeIsDarkMode(): Flow<Boolean> {
        return userPreferencesDataStore.isDarkMode
    }

    override suspend fun setDarkMode(isDarkMode: Boolean) {
        userPreferencesDataStore.setDarkMode(isDarkMode)
    }
}
