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
        private const val OIL_CHANGE_INTERVAL = 5000
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
                            val nextMaintenance = calculateNextMaintenance(car, maintenanceList)

                            _uiState.update {
                                it.copy(
                                    car = car,
                                    recentMaintenanceList = recentList,
                                    nextMaintenance = nextMaintenance,
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
                                nextMaintenance = null,
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

    private fun calculateNextMaintenance(
        car: Car,
        maintenanceList: List<Maintenance>
    ): NextMaintenance? {
        val lastOilChange = maintenanceList
            .filter { it.type == MaintenanceType.OIL_CHANGE }
            .maxByOrNull { it.date }

        return if (lastOilChange != null) {
            val distanceSinceLastOilChange = car.mileage - lastOilChange.mileage
            val remainingDistance = OIL_CHANGE_INTERVAL - distanceSinceLastOilChange
            val progress = distanceSinceLastOilChange.toFloat() / OIL_CHANGE_INTERVAL

            NextMaintenance(
                type = MaintenanceType.OIL_CHANGE,
                remainingDistance = remainingDistance.coerceAtLeast(0),
                progressPercentage = progress.coerceIn(0f, 1f)
            )
        } else {
            NextMaintenance(
                type = MaintenanceType.OIL_CHANGE,
                remainingDistance = OIL_CHANGE_INTERVAL,
                progressPercentage = 0f
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
