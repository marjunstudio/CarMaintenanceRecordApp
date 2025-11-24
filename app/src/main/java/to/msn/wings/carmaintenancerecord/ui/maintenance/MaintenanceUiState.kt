package to.msn.wings.carmaintenancerecord.ui.maintenance

import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType

data class MaintenanceListUiState(
    val maintenanceList: List<Maintenance> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

data class MaintenanceDetailUiState(
    val maintenance: Maintenance? = null,
    val selectedType: MaintenanceType = MaintenanceType.OIL_CHANGE,
    val dateMillis: Long = System.currentTimeMillis(),
    val mileageText: String = "",
    val memoText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
) {
    fun validateInput(): String? {
        // 走行距離のバリデーション
        if (mileageText.isBlank()) {
            return "走行距離を入力してください"
        }

        val mileage = mileageText.toIntOrNull()
        if (mileage == null || mileage < 0) {
            return "走行距離は0以上の数値で入力してください"
        }

        return null
    }

    fun toMaintenance(carId: Long): Maintenance {
        val now = System.currentTimeMillis()
        return Maintenance(
            id = maintenance?.id ?: 0L,
            carId = carId,
            type = selectedType,
            date = dateMillis,
            mileage = mileageText.toInt(),
            memo = memoText.ifBlank { null },
            createdAt = maintenance?.createdAt ?: now,
            updatedAt = now
        )
    }

    companion object {
        fun fromMaintenance(maintenance: Maintenance): MaintenanceDetailUiState {
            return MaintenanceDetailUiState(
                maintenance = maintenance,
                selectedType = maintenance.type,
                dateMillis = maintenance.date,
                mileageText = maintenance.mileage.toString(),
                memoText = maintenance.memo ?: ""
            )
        }
    }
}
