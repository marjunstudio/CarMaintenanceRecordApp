package to.msn.wings.carmaintenancerecord.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import to.msn.wings.carmaintenancerecord.data.local.dao.MaintenanceDao
import to.msn.wings.carmaintenancerecord.data.mapper.toDomain
import to.msn.wings.carmaintenancerecord.data.mapper.toDomainList
import to.msn.wings.carmaintenancerecord.data.mapper.toEntity
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaintenanceRepositoryImpl @Inject constructor(
    private val maintenanceDao: MaintenanceDao
) : MaintenanceRepository {

    override suspend fun insertMaintenance(maintenance: Maintenance): Long {
        return maintenanceDao.insert(maintenance.toEntity())
    }

    override suspend fun updateMaintenance(maintenance: Maintenance) {
        maintenanceDao.update(maintenance.toEntity())
    }

    override suspend fun deleteMaintenance(maintenance: Maintenance) {
        maintenanceDao.delete(maintenance.toEntity())
    }

    override suspend fun getMaintenanceById(id: Long): Maintenance? {
        return maintenanceDao.getById(id)?.toDomain()
    }

    override fun getMaintenanceByIdFlow(id: Long): Flow<Maintenance?> {
        return maintenanceDao.getByIdFlow(id).map { it?.toDomain() }
    }

    override suspend fun getMaintenanceListByCarId(carId: Long): List<Maintenance> {
        return maintenanceDao.getListByCarId(carId).toDomainList()
    }

    override fun getMaintenanceListByCarIdFlow(carId: Long): Flow<List<Maintenance>> {
        return maintenanceDao.getListByCarIdFlow(carId).map { it.toDomainList() }
    }

    override suspend fun getMaintenanceListByCarIdAndType(
        carId: Long,
        type: MaintenanceType
    ): List<Maintenance> {
        return maintenanceDao.getListByCarIdAndType(carId, type.name).toDomainList()
    }

    override suspend fun getLatestMaintenanceByType(carId: Long): List<Maintenance> {
        return maintenanceDao.getLatestByType(carId).toDomainList()
    }

    override fun getLatestMaintenanceByTypeFlow(carId: Long): Flow<List<Maintenance>> {
        return maintenanceDao.getLatestByTypeFlow(carId).map { it.toDomainList() }
    }

    override suspend fun getRecentMaintenanceList(carId: Long, limit: Int): List<Maintenance> {
        return maintenanceDao.getRecentList(carId, limit).toDomainList()
    }

    override fun getRecentMaintenanceListFlow(carId: Long, limit: Int): Flow<List<Maintenance>> {
        return maintenanceDao.getRecentListFlow(carId, limit).map { it.toDomainList() }
    }
}
