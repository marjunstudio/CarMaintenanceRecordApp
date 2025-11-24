package to.msn.wings.carmaintenancerecord.ui.car

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import to.msn.wings.carmaintenancerecord.domain.model.Car
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceIntervalSetting
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import to.msn.wings.carmaintenancerecord.domain.usecase.GetCarUseCase
import to.msn.wings.carmaintenancerecord.domain.usecase.GetMaintenanceIntervalSettingsUseCase
import to.msn.wings.carmaintenancerecord.domain.usecase.GetMaintenanceListUseCase
import javax.inject.Inject

@HiltViewModel
class CarDetailViewModel @Inject constructor(
    private val getCarUseCase: GetCarUseCase,
    private val getMaintenanceListUseCase: GetMaintenanceListUseCase,
    private val getIntervalSettingsUseCase: GetMaintenanceIntervalSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarDetailUiState())
    val uiState: StateFlow<CarDetailUiState> = _uiState.asStateFlow()

    companion object {
        private const val MAX_NEXT_MAINTENANCE_DISPLAY = 3
        private const val MILLIS_PER_DAY = 86400000L
    }

    init {
        loadCarAndMaintenanceData()
    }

    private fun loadCarAndMaintenanceData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                getCarUseCase().collect { car ->
                    if (car != null) {
                        combine(
                            getMaintenanceListUseCase(car.id),
                            getIntervalSettingsUseCase(car.id)
                        ) { maintenanceList, intervalSettings ->
                            val recentList = maintenanceList.take(3)
                            val nextMaintenanceList = calculateNextMaintenances(
                                car = car,
                                maintenanceList = maintenanceList,
                                intervalSettings = intervalSettings
                            )

                            CarDetailUiState(
                                car = car,
                                recentMaintenanceList = recentList,
                                nextMaintenanceList = nextMaintenanceList,
                                isLoading = false,
                                errorMessage = null
                            )
                        }.collect { newState ->
                            _uiState.value = newState
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                car = null,
                                recentMaintenanceList = emptyList(),
                                nextMaintenanceList = emptyList(),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "データの読み込みに失敗しました: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * すべてのメンテナンス種類の次回予定を計算
     * ユーザー設定の周期を優先し、なければデフォルト値を使用
     * 進捗率の高い順（=実施時期が近い順）にソートして、上位を返す
     */
    private fun calculateNextMaintenances(
        car: Car,
        maintenanceList: List<Maintenance>,
        intervalSettings: List<MaintenanceIntervalSetting>
    ): List<NextMaintenance> {
        val currentTime = System.currentTimeMillis()
        val nextMaintenances = mutableListOf<NextMaintenance>()
        val settingsMap = intervalSettings.associateBy { it.type }

        MaintenanceType.values().forEach { type ->
            val customSetting = settingsMap[type]
            val intervalKm = customSetting?.intervalKm ?: type.defaultIntervalKm
            val intervalDays = customSetting?.intervalDays ?: type.defaultIntervalDays

            val lastMaintenance = maintenanceList
                .filter { it.type == type }
                .maxByOrNull { it.date }

            val candidates = mutableListOf<NextMaintenance>()

            // 距離ベースの計算
            if (intervalKm != null && intervalKm > 0) {
                val nextMaintenance = if (lastMaintenance != null) {
                    val distanceSinceLast = car.mileage - lastMaintenance.mileage
                    val remainingDistance = intervalKm - distanceSinceLast
                    val progress = distanceSinceLast.toFloat() / intervalKm

                    NextMaintenance(
                        type = type,
                        remainingDistance = remainingDistance.coerceAtLeast(0),
                        progressPercentage = progress.coerceIn(0f, 1f)
                    )
                } else {
                    val distanceSinceDelivery = car.mileage - car.initialMileage
                    val remainingDistance = intervalKm - distanceSinceDelivery
                    val progress = distanceSinceDelivery.toFloat() / intervalKm

                    NextMaintenance(
                        type = type,
                        remainingDistance = remainingDistance.coerceAtLeast(0),
                        progressPercentage = progress.coerceIn(0f, 1f)
                    )
                }
                candidates.add(nextMaintenance)
            }

            // 日数ベースの計算
            if (intervalDays != null && intervalDays > 0) {
                val nextMaintenance = if (lastMaintenance != null) {
                    val daysSinceLast = ((currentTime - lastMaintenance.date) / MILLIS_PER_DAY).toInt()
                    val remainingDays = intervalDays - daysSinceLast
                    val progress = daysSinceLast.toFloat() / intervalDays

                    NextMaintenance(
                        type = type,
                        remainingDays = remainingDays.coerceAtLeast(0),
                        progressPercentage = progress.coerceIn(0f, 1f)
                    )
                } else {
                    val daysSinceCreation = ((currentTime - car.createdAt) / MILLIS_PER_DAY).toInt()
                    val remainingDays = intervalDays - daysSinceCreation
                    val progress = daysSinceCreation.toFloat() / intervalDays

                    NextMaintenance(
                        type = type,
                        remainingDays = remainingDays.coerceAtLeast(0),
                        progressPercentage = progress.coerceIn(0f, 1f)
                    )
                }
                candidates.add(nextMaintenance)
            }

            // 両方設定されている場合は、より緊急な方（進捗率が高い方）を採用
            if (candidates.isNotEmpty()) {
                val mostUrgent = candidates.maxByOrNull { it.progressPercentage }
                if (mostUrgent != null) {
                    nextMaintenances.add(mostUrgent)
                }
            }
        }

        return nextMaintenances
            .sortedByDescending { it.progressPercentage }
            .take(MAX_NEXT_MAINTENANCE_DISPLAY)
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
