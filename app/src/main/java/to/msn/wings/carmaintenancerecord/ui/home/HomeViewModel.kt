package to.msn.wings.carmaintenancerecord.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import to.msn.wings.carmaintenancerecord.domain.usecase.GetCarUseCase
import to.msn.wings.carmaintenancerecord.domain.usecase.GetMaintenanceListUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCarUseCase: GetCarUseCase,
    private val getMaintenanceListUseCase: GetMaintenanceListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                getCarUseCase().collect { car ->
                    _uiState.update { it.copy(car = car, isLoading = false) }

                    car?.let {
                        loadMaintenances(it.id)
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

    private fun loadMaintenances(carId: Long) {
        viewModelScope.launch {
            try {
                getMaintenanceListUseCase(carId).collect { maintenances ->
                    _uiState.update {
                        it.copy(recentMaintenances = maintenances.take(3))
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "メンテナンス履歴の読み込みに失敗しました: ${e.message}")
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
