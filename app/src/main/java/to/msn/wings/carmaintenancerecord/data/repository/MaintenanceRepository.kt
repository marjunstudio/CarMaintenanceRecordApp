package to.msn.wings.carmaintenancerecord.data.repository

import kotlinx.coroutines.flow.Flow
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType

interface MaintenanceRepository {

    suspend fun insertMaintenance(maintenance: Maintenance): Long
    suspend fun updateMaintenance(maintenance: Maintenance)
    suspend fun deleteMaintenance(maintenance: Maintenance)
    suspend fun getMaintenanceById(id: Long): Maintenance?
    fun getMaintenanceByIdFlow(id: Long): Flow<Maintenance?>
    suspend fun getMaintenanceListByCarId(carId: Long): List<Maintenance>
    fun getMaintenanceListByCarIdFlow(carId: Long): Flow<List<Maintenance>>
    suspend fun getMaintenanceListByCarIdAndType(carId: Long, type: MaintenanceType): List<Maintenance>
    suspend fun getLatestMaintenanceByType(carId: Long): List<Maintenance>
    fun getLatestMaintenanceByTypeFlow(carId: Long): Flow<List<Maintenance>>
    suspend fun getRecentMaintenanceList(carId: Long, limit: Int): List<Maintenance>
    fun getRecentMaintenanceListFlow(carId: Long, limit: Int): Flow<List<Maintenance>>
}
