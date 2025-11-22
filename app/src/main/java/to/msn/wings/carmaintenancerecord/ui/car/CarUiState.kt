package to.msn.wings.carmaintenancerecord.ui.car

import to.msn.wings.carmaintenancerecord.domain.model.Car

data class CarUiState(
    val car: Car? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val carName: String = "",
    val carManufacturer: String = "",
    val carModel: String = "",
    val carYear: String = "",
    val carLicensePlate: String = "",
    val carInitialMileage: String = "",
    val carMileage: String = ""
)
