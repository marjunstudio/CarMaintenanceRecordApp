package to.msn.wings.carmaintenancerecord.domain.usecase

import to.msn.wings.carmaintenancerecord.data.repository.MaintenanceRepository
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import javax.inject.Inject

/**
 * メンテナンス記録を保存するUseCase
 *
 * メンテナンス記録の新規作成または更新を行います。
 * IDが0の場合は新規作成、それ以外の場合は更新として処理されます。
 *
 * @property repository メンテナンス情報のリポジトリ
 */
class SaveMaintenanceUseCase @Inject constructor(
    private val repository: MaintenanceRepository
) {
    /**
     * メンテナンス記録を保存
     *
     * @param maintenance 保存するメンテナンス記録
     * @return 保存されたメンテナンス記録のID
     */
    suspend operator fun invoke(maintenance: Maintenance): Long {
        return if (maintenance.isNew()) {
            // 新規作成
            repository.insertMaintenance(maintenance)
        } else {
            // 更新
            val updatedMaintenance = maintenance.copy(
                updatedAt = System.currentTimeMillis()
            )
            repository.updateMaintenance(updatedMaintenance)
            maintenance.id
        }
    }
}
