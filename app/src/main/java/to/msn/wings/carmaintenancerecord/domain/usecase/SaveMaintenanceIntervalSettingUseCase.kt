package to.msn.wings.carmaintenancerecord.domain.usecase

import to.msn.wings.carmaintenancerecord.data.repository.MaintenanceIntervalSettingRepository
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceIntervalSetting
import javax.inject.Inject

/**
 * メンテナンス周期設定を保存するUseCase
 */
class SaveMaintenanceIntervalSettingUseCase @Inject constructor(
    private val repository: MaintenanceIntervalSettingRepository
) {
    suspend operator fun invoke(setting: MaintenanceIntervalSetting): Long {
        return repository.saveSetting(setting)
    }
}
