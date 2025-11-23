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
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import to.msn.wings.carmaintenancerecord.domain.usecase.GetCarUseCase
import to.msn.wings.carmaintenancerecord.domain.usecase.GetMaintenanceListUseCase
import javax.inject.Inject

@HiltViewModel
class CarDetailViewModel @Inject constructor(
    private val getCarUseCase: GetCarUseCase,
    private val getMaintenanceListUseCase: GetMaintenanceListUseCase
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
                        getMaintenanceListUseCase(car.id).collect { maintenanceList ->
                            val recentList = maintenanceList.take(3)
                            val nextMaintenanceList = calculateNextMaintenances(car, maintenanceList)

                            _uiState.update {
                                it.copy(
                                    car = car,
                                    recentMaintenanceList = recentList,
                                    nextMaintenanceList = nextMaintenanceList,
                                    isLoading = false,
                                    errorMessage = null
                                )
                            }
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
     * 進捗率の高い順（=実施時期が近い順）にソートして、上位を返す
     */
    private fun calculateNextMaintenances(
        car: Car,
        maintenanceList: List<Maintenance>
    ): List<NextMaintenance> {
        val currentTime = System.currentTimeMillis()
        val nextMaintenances = mutableListOf<NextMaintenance>()

        MaintenanceType.values().forEach { type ->
            val lastMaintenance = maintenanceList
                .filter { it.type == type }
                .maxByOrNull { it.date }

            when {
                // 距離ベースのメンテナンス
                type.defaultIntervalKm != null -> {
                    val interval = type.defaultIntervalKm
                    val nextMaintenance = if (lastMaintenance != null) {
                        val distanceSinceLast = car.mileage - lastMaintenance.mileage
                        val remainingDistance = interval - distanceSinceLast
                        val progress = distanceSinceLast.toFloat() / interval

                        NextMaintenance(
                            type = type,
                            remainingDistance = remainingDistance.coerceAtLeast(0),
                            progressPercentage = progress.coerceIn(0f, 1f)
                        )
                    } else {
                        val distanceSinceDelivery = car.mileage - car.initialMileage
                        val remainingDistance = interval - distanceSinceDelivery
                        val progress = distanceSinceDelivery.toFloat() / interval

                        NextMaintenance(
                            type = type,
                            remainingDistance = remainingDistance.coerceAtLeast(0),
                            progressPercentage = progress.coerceIn(0f, 1f)
                        )
                    }
                    nextMaintenances.add(nextMaintenance)
                }

                // 日数ベースのメンテナンス
                type.defaultIntervalDays != null -> {
                    val interval = type.defaultIntervalDays
                    val nextMaintenance = if (lastMaintenance != null) {
                        val daysSinceLast = ((currentTime - lastMaintenance.date) / MILLIS_PER_DAY).toInt()
                        val remainingDays = interval - daysSinceLast
                        val progress = daysSinceLast.toFloat() / interval

                        NextMaintenance(
                            type = type,
                            remainingDays = remainingDays.coerceAtLeast(0),
                            progressPercentage = progress.coerceIn(0f, 1f)
                        )
                    } else {
                        val daysSinceCreation = ((currentTime - car.createdAt) / MILLIS_PER_DAY).toInt()
                        val remainingDays = interval - daysSinceCreation
                        val progress = daysSinceCreation.toFloat() / interval

                        NextMaintenance(
                            type = type,
                            remainingDays = remainingDays.coerceAtLeast(0),
                            progressPercentage = progress.coerceIn(0f, 1f)
                        )
                    }
                    nextMaintenances.add(nextMaintenance)
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
