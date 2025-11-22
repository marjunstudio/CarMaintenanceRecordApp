package to.msn.wings.carmaintenancerecord.domain.usecase

import kotlinx.coroutines.flow.Flow
import to.msn.wings.carmaintenancerecord.data.repository.MaintenanceRepository
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import javax.inject.Inject

/**
 * メンテナンス履歴一覧を取得するUseCase
 *
 * 指定された車両のメンテナンス履歴を、最新順（実施日時の降順）で取得。
 * Flowとして返すため、データベースの変更がリアルタイムに反映される。
 *
 * @property repository メンテナンス情報のリポジトリ
 */
class GetMaintenanceListUseCase @Inject constructor(
    private val repository: MaintenanceRepository
) {

    operator fun invoke(carId: Long): Flow<List<Maintenance>> {
        return repository.getMaintenanceListByCarIdFlow(carId)
    }
}
