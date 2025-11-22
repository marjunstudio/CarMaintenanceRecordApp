package to.msn.wings.carmaintenancerecord.ui.home

import to.msn.wings.carmaintenancerecord.domain.model.Car
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance

data class HomeUiState(
    val car: Car? = null,
    val recentMaintenances: List<Maintenance> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
