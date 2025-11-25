package to.msn.wings.carmaintenancerecord.ui.settings.maintenance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceIntervalSetting
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import to.msn.wings.carmaintenancerecord.domain.usecase.GetCarUseCase
import to.msn.wings.carmaintenancerecord.domain.usecase.GetMaintenanceIntervalSettingsUseCase
import to.msn.wings.carmaintenancerecord.domain.usecase.ResetMaintenanceIntervalSettingUseCase
import to.msn.wings.carmaintenancerecord.domain.usecase.SaveMaintenanceIntervalSettingUseCase
import javax.inject.Inject

@HiltViewModel
class MaintenanceIntervalSettingViewModel @Inject constructor(
    private val getCarUseCase: GetCarUseCase,
    private val getSettingsUseCase: GetMaintenanceIntervalSettingsUseCase,
    private val saveSettingUseCase: SaveMaintenanceIntervalSettingUseCase,
    private val resetSettingUseCase: ResetMaintenanceIntervalSettingUseCase
) : ViewModel() {

    private val errorMessageFlow = MutableStateFlow<String?>(null)
    private val _uiState = MutableStateFlow(MaintenanceIntervalSettingUiState(isLoading = true))
    val uiState: StateFlow<MaintenanceIntervalSettingUiState> = _uiState

    init {
        viewModelScope.launch {
            getCarUseCase()
                .combine(errorMessageFlow) { car, errorMessage ->
                    car to errorMessage
                }
                .collect { (car, errorMessage) ->
                    if (car == null) {
                        _uiState.value = MaintenanceIntervalSettingUiState(
                            isLoading = false,
                            errorMessage = errorMessage ?: "車両情報が見つかりません"
                        )
                    } else {
                        getSettingsUseCase(car.id)
                            .catch { e ->
                                errorMessageFlow.value = "設定の読み込みに失敗しました: ${e.message}"
                                emit(emptyList())
                            }
                            .collect { settings ->
                                _uiState.value = MaintenanceIntervalSettingUiState(
                                    carId = car.id,
                                    settingItems = createSettingItems(settings),
                                    isLoading = false,
                                    errorMessage = errorMessageFlow.value
                                )
                            }
                    }
                }
        }
    }

    private fun createSettingItems(settings: List<MaintenanceIntervalSetting>): List<SettingItem> {
        val settingsMap = settings.associateBy { it.type }

        return MaintenanceType.entries
            .filter { it != MaintenanceType.OTHER }
            .map { type ->
                val customSetting = settingsMap[type]
                SettingItem(
                    type = type,
                    customIntervalKm = customSetting?.intervalKm,
                    customIntervalDays = customSetting?.intervalDays,
                    defaultIntervalKm = type.defaultIntervalKm,
                    defaultIntervalDays = type.defaultIntervalDays
                )
            }
    }

    fun saveSetting(type: MaintenanceType, intervalKm: Int?, intervalDays: Int?) {
        val carId = uiState.value.carId ?: return

        viewModelScope.launch {
            try {
                val setting = MaintenanceIntervalSetting(
                    carId = carId,
                    type = type,
                    intervalKm = intervalKm,
                    intervalDays = intervalDays
                )
                saveSettingUseCase(setting)
                errorMessageFlow.value = null
            } catch (e: Exception) {
                errorMessageFlow.value = "設定の保存に失敗しました: ${e.message}"
            }
        }
    }

    fun resetSetting(type: MaintenanceType) {
        val carId = uiState.value.carId ?: return

        viewModelScope.launch {
            try {
                resetSettingUseCase(carId, type)
                errorMessageFlow.value = null
            } catch (e: Exception) {
                errorMessageFlow.value = "設定のリセットに失敗しました: ${e.message}"
            }
        }
    }

    fun clearError() {
        errorMessageFlow.value = null
    }
}
