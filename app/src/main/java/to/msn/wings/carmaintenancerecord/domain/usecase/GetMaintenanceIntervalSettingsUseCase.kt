package to.msn.wings.carmaintenancerecord.domain.usecase

import kotlinx.coroutines.flow.Flow
import to.msn.wings.carmaintenancerecord.data.repository.MaintenanceIntervalSettingRepository
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceIntervalSetting
import javax.inject.Inject

/**
 * 車両のメンテナンス周期設定を取得するUseCase
 */
class GetMaintenanceIntervalSettingsUseCase @Inject constructor(
    private val repository: MaintenanceIntervalSettingRepository
) {
    operator fun invoke(carId: Long): Flow<List<MaintenanceIntervalSetting>> {
        return repository.getSettingsByCarIdFlow(carId)
    }
}
