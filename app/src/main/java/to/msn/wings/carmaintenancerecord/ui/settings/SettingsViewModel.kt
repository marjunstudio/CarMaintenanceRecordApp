package to.msn.wings.carmaintenancerecord.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import to.msn.wings.carmaintenancerecord.BuildConfig
import to.msn.wings.carmaintenancerecord.data.repository.UserPreferencesRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = userPreferencesRepository
        .observeIsDarkMode()
        .combine(kotlinx.coroutines.flow.flowOf(BuildConfig.VERSION_NAME)) { isDarkMode, version ->
            SettingsUiState(
                appVersion = version,
                isDarkMode = isDarkMode
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState(appVersion = BuildConfig.VERSION_NAME)
        )

    fun toggleDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDarkMode(isDarkMode)
        }
    }
}
