package to.msn.wings.carmaintenancerecord.domain.usecase

import to.msn.wings.carmaintenancerecord.data.repository.MaintenanceRepository
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import javax.inject.Inject

/**
 * メンテナンス記録の詳細を取得するUseCase
 *
 * 指定されたIDのメンテナンス記録を取得します。
 *
 * @property repository メンテナンス情報のリポジトリ
 */
class GetMaintenanceByIdUseCase @Inject constructor(
    private val repository: MaintenanceRepository
) {
    /**
     * 指定されたIDのメンテナンス記録を取得
     *
     * @param id メンテナンス記録ID
     * @return メンテナンス記録、見つからない場合は null
     */
    suspend operator fun invoke(id: Long): Maintenance? {
        return repository.getMaintenanceById(id)
    }
}
