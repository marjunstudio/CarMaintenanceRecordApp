package to.msn.wings.carmaintenancerecord.ui.car

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import to.msn.wings.carmaintenancerecord.domain.model.Car
import to.msn.wings.carmaintenancerecord.domain.usecase.GetCarUseCase
import to.msn.wings.carmaintenancerecord.domain.usecase.SaveCarUseCase
import to.msn.wings.carmaintenancerecord.domain.usecase.UpdateMileageUseCase
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    private val getCarUseCase: GetCarUseCase,
    private val saveCarUseCase: SaveCarUseCase,
    private val updateMileageUseCase: UpdateMileageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarUiState())
    val uiState: StateFlow<CarUiState> = _uiState.asStateFlow()

    init {
        loadCar()
    }

    private fun loadCar() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                getCarUseCase().collect { car ->
                    _uiState.update {
                        it.copy(
                            car = car,
                            carName = car?.name ?: "",
                            carManufacturer = car?.manufacturer ?: "",
                            carModel = car?.model ?: "",
                            carYear = car?.year?.toString() ?: "",
                            carLicensePlate = car?.licensePlate ?: "",
                            carInitialMileage = car?.initialMileage?.toString() ?: "",
                            carMileage = car?.mileage?.toString() ?: "",
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "車両情報の読み込みに失敗しました: ${e.message}"
                    )
                }
            }
        }
    }

    fun onCarNameChanged(name: String) {
        _uiState.update { it.copy(carName = name) }
    }

    fun onCarManufacturerChanged(manufacturer: String) {
        _uiState.update { it.copy(carManufacturer = manufacturer) }
    }

    fun onCarModelChanged(model: String) {
        _uiState.update { it.copy(carModel = model) }
    }

    fun onCarYearChanged(year: String) {
        _uiState.update { it.copy(carYear = year) }
    }

    fun onCarLicensePlateChanged(licensePlate: String) {
        _uiState.update { it.copy(carLicensePlate = licensePlate) }
    }

    fun onCarInitialMileageChanged(initialMileage: String) {
        _uiState.update { it.copy(carInitialMileage = initialMileage) }
    }

    fun onCarMileageChanged(mileage: String) {
        _uiState.update { it.copy(carMileage = mileage) }
    }

    fun saveCar() {
        val currentState = _uiState.value

        // バリデーション
        if (currentState.carName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "車名を入力してください") }
            return
        }

        if (currentState.carManufacturer.isBlank()) {
            _uiState.update { it.copy(errorMessage = "メーカーを入力してください") }
            return
        }

        if (currentState.carModel.isBlank()) {
            _uiState.update { it.copy(errorMessage = "車種を入力してください") }
            return
        }

        val year = currentState.carYear.toIntOrNull()
        if (year == null || year < 1900 || year > 2100) {
            _uiState.update { it.copy(errorMessage = "正しい年式を入力してください") }
            return
        }

        val initialMileage = currentState.carInitialMileage.toIntOrNull() ?: 0
        if (initialMileage < 0) {
            _uiState.update { it.copy(errorMessage = "正しい納車時走行距離を入力してください") }
            return
        }

        val mileage = currentState.carMileage.toIntOrNull()
        if (mileage == null || mileage < 0) {
            _uiState.update { it.copy(errorMessage = "正しい走行距離を入力してください") }
            return
        }

        if (mileage < initialMileage) {
            _uiState.update { it.copy(errorMessage = "現在の走行距離は納車時より少なくできません") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                val currentTime = System.currentTimeMillis()
                val car = Car(
                    name = currentState.carName,
                    manufacturer = currentState.carManufacturer,
                    model = currentState.carModel,
                    year = year,
                    licensePlate = currentState.carLicensePlate.ifBlank { null },
                    initialMileage = initialMileage,
                    mileage = mileage,
                    photoUri = null,
                    createdAt = currentTime,
                    updatedAt = currentTime
                )
                saveCarUseCase(car)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "保存に失敗しました: ${e.message}"
                    )
                }
            }
        }
    }

    fun updateMileage() {
        val currentState = _uiState.value
        val car = currentState.car

        if (car == null) {
            _uiState.update { it.copy(errorMessage = "車両が登録されていません") }
            return
        }

        val mileage = currentState.carMileage.toIntOrNull()
        if (mileage == null || mileage < 0) {
            _uiState.update { it.copy(errorMessage = "正しい走行距離を入力してください") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                updateMileageUseCase(car, mileage)
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "更新に失敗しました: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
