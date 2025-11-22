package to.msn.wings.carmaintenancerecord.ui.maintenance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import to.msn.wings.carmaintenancerecord.domain.usecase.GetMaintenanceListUseCase
import javax.inject.Inject

@HiltViewModel
class MaintenanceListViewModel @Inject constructor(
    private val getMaintenanceListUseCase: GetMaintenanceListUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(MaintenanceListUiState())
    val uiState: StateFlow<MaintenanceListUiState> = _uiState.asStateFlow()

    init {
        // SavedStateHandleからcarIdを取得（デフォルトは1L）
        val carId = savedStateHandle.get<Long>("carId") ?: 1L
        loadMaintenanceList(carId)
    }

    private fun loadMaintenanceList(carId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            getMaintenanceListUseCase(carId)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "メンテナンス履歴の読み込みに失敗しました: ${e.message}"
                        )
                    }
                }
                .collect { maintenanceList ->
                    _uiState.update {
                        it.copy(
                            maintenanceList = maintenanceList,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
