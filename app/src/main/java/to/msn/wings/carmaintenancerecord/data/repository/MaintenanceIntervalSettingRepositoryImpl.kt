package to.msn.wings.carmaintenancerecord.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import to.msn.wings.carmaintenancerecord.data.local.dao.MaintenanceIntervalSettingDao
import to.msn.wings.carmaintenancerecord.data.mapper.toDomain
import to.msn.wings.carmaintenancerecord.data.mapper.toDomainList
import to.msn.wings.carmaintenancerecord.data.mapper.toEntity
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceIntervalSetting
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import javax.inject.Inject

/**
 * メンテナンス周期設定リポジトリの実装
 */
class MaintenanceIntervalSettingRepositoryImpl @Inject constructor(
    private val dao: MaintenanceIntervalSettingDao
) : MaintenanceIntervalSettingRepository {

    override fun getSettingsByCarIdFlow(carId: Long): Flow<List<MaintenanceIntervalSetting>> {
        return dao.getSettingsByCarIdFlow(carId).map { it.toDomainList() }
    }

    override suspend fun getSettingsByCarId(carId: Long): List<MaintenanceIntervalSetting> {
        return dao.getSettingsByCarId(carId).toDomainList()
    }

    override suspend fun getSettingByCarIdAndType(
        carId: Long,
        type: MaintenanceType
    ): MaintenanceIntervalSetting? {
        return dao.getSettingByCarIdAndType(carId, type.name)?.toDomain()
    }

    override suspend fun saveSetting(setting: MaintenanceIntervalSetting): Long {
        return dao.insertOrUpdate(setting.toEntity())
    }

    override suspend fun deleteSetting(carId: Long, type: MaintenanceType) {
        dao.deleteByCarIdAndType(carId, type.name)
    }

    override suspend fun deleteAllSettings(carId: Long) {
        dao.deleteByCarId(carId)
    }
}
