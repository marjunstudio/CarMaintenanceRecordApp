package to.msn.wings.carmaintenancerecord.domain.usecase

import to.msn.wings.carmaintenancerecord.data.repository.MaintenanceIntervalSettingRepository
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import javax.inject.Inject

/**
 * メンテナンス周期設定をデフォルトに戻す（削除する）UseCase
 */
class ResetMaintenanceIntervalSettingUseCase @Inject constructor(
    private val repository: MaintenanceIntervalSettingRepository
) {
    suspend operator fun invoke(carId: Long, type: MaintenanceType) {
        repository.deleteSetting(carId, type)
    }
}
