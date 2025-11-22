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
class CarEditViewModel @Inject constructor(
    private val getCarUseCase: GetCarUseCase,
    private val saveCarUseCase: SaveCarUseCase,
    private val updateMileageUseCase: UpdateMileageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarEditUiState())
    val uiState: StateFlow<CarEditUiState> = _uiState.asStateFlow()

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

        _uiState.update {
            it.copy(
                carNameError = null,
                carManufacturerError = null,
                carModelError = null,
                carYearError = null,
                carMileageError = null
            )
        }

        var hasError = false

        if (currentState.carName.isBlank()) {
            _uiState.update { it.copy(carNameError = "愛車の名前を入力してください") }
            hasError = true
        }

        if (currentState.carManufacturer.isBlank()) {
            _uiState.update { it.copy(carManufacturerError = "メーカーを選択してください") }
            hasError = true
        }

        if (currentState.carModel.isBlank()) {
            _uiState.update { it.copy(carModelError = "車種を入力してください") }
            hasError = true
        }

        val year = if (currentState.carYear.isBlank()) {
            _uiState.update { it.copy(carYearError = "年式を入力してください") }
            hasError = true
            null
        } else {
            currentState.carYear.toIntOrNull().also {
                if (it == null || it < 1900 || it > 2100) {
                    _uiState.update { state -> state.copy(carYearError = "正しい年式を入力してください（1900〜2100）") }
                    hasError = true
                }
            }
        }

        val initialMileage = if (currentState.carInitialMileage.isNotBlank()) {
            currentState.carInitialMileage.toIntOrNull().also {
                if (it == null || it < 0) {
                    _uiState.update { state -> state.copy(errorMessage = "正しい納車時走行距離を入力してください") }
                    hasError = true
                }
            } ?: 0
        } else {
            0
        }

        val mileage = if (currentState.carMileage.isNotBlank()) {
            currentState.carMileage.toIntOrNull().also {
                if (it == null || it < 0) {
                    _uiState.update { state -> state.copy(carMileageError = "走行距離は数字で入力してください") }
                    hasError = true
                } else if (initialMileage > 0 && it < initialMileage) {
                    _uiState.update { state -> state.copy(carMileageError = "現在の走行距離は納車時より少なくできません") }
                    hasError = true
                }
            } ?: 0
        } else {
            0
        }

        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                val currentTime = System.currentTimeMillis()
                val car = Car(
                    name = currentState.carName,
                    manufacturer = currentState.carManufacturer,
                    model = currentState.carModel,
                    year = year ?: 0,
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
