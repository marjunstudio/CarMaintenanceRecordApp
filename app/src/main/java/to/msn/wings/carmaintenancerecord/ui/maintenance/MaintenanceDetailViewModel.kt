package to.msn.wings.carmaintenancerecord.ui.maintenance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import to.msn.wings.carmaintenancerecord.domain.usecase.GetMaintenanceByIdUseCase
import to.msn.wings.carmaintenancerecord.domain.usecase.SaveMaintenanceUseCase
import javax.inject.Inject

/**
 * メンテナンス記録詳細・登録画面のViewModel
 *
 * @property getMaintenanceByIdUseCase メンテナンス記録取得UseCase
 * @property saveMaintenanceUseCase メンテナンス記録保存UseCase
 * @property savedStateHandle SavedStateHandle（Navigationからのパラメータ取得用）
 */
@HiltViewModel
class MaintenanceDetailViewModel @Inject constructor(
    private val getMaintenanceByIdUseCase: GetMaintenanceByIdUseCase,
    private val saveMaintenanceUseCase: SaveMaintenanceUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(MaintenanceDetailUiState())
    val uiState: StateFlow<MaintenanceDetailUiState> = _uiState.asStateFlow()

    private val carId: Long = savedStateHandle.get<Long>("carId") ?: 1L

    init {
        // SavedStateHandleからmaintenanceIdを取得
        val maintenanceId = savedStateHandle.get<Long>("maintenanceId")
        if (maintenanceId != null && maintenanceId > 0L) {
            loadMaintenance(maintenanceId)
        }
    }

    /**
     * 既存のメンテナンス記録を読み込む
     *
     * @param maintenanceId メンテナンス記録ID
     */
    private fun loadMaintenance(maintenanceId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val maintenance = getMaintenanceByIdUseCase(maintenanceId)
                if (maintenance != null) {
                    _uiState.value = MaintenanceDetailUiState.fromMaintenance(maintenance)
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "メンテナンス記録が見つかりません"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "メンテナンス記録の読み込みに失敗しました: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * メンテナンス種別を更新
     *
     * @param type 新しいメンテナンス種別
     */
    fun updateType(type: MaintenanceType) {
        _uiState.update { it.copy(selectedType = type) }
    }

    /**
     * 実施日時を更新
     *
     * @param dateMillis 新しい実施日時（ミリ秒）
     */
    fun updateDate(dateMillis: Long) {
        _uiState.update { it.copy(dateMillis = dateMillis) }
    }

    /**
     * 走行距離を更新
     *
     * @param mileage 新しい走行距離のテキスト
     */
    fun updateMileage(mileage: String) {
        _uiState.update { it.copy(mileageText = mileage) }
    }

    /**
     * メモを更新
     *
     * @param memo 新しいメモのテキスト
     */
    fun updateMemo(memo: String) {
        _uiState.update { it.copy(memoText = memo) }
    }

    /**
     * メンテナンス記録を保存
     */
    fun saveMaintenance() {
        viewModelScope.launch {
            val currentState = _uiState.value

            // バリデーション
            val validationError = currentState.validateInput()
            if (validationError != null) {
                _uiState.update { it.copy(errorMessage = validationError) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val maintenance = currentState.toMaintenance(carId)
                saveMaintenanceUseCase(maintenance)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSaved = true,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "保存に失敗しました: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * エラーメッセージをクリア
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * 保存完了フラグをリセット
     */
    fun resetSavedFlag() {
        _uiState.update { it.copy(isSaved = false) }
    }
}
