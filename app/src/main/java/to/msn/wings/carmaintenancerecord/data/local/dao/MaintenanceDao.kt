package to.msn.wings.carmaintenancerecord.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import to.msn.wings.carmaintenancerecord.data.local.entity.MaintenanceEntity

@Dao
interface MaintenanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(maintenance: MaintenanceEntity): Long

    @Update
    suspend fun update(maintenance: MaintenanceEntity)

    @Delete
    suspend fun delete(maintenance: MaintenanceEntity)

    @Query("SELECT * FROM maintenances WHERE id = :id")
    suspend fun getById(id: Long): MaintenanceEntity?

    @Query("SELECT * FROM maintenances WHERE id = :id")
    fun getByIdFlow(id: Long): Flow<MaintenanceEntity?>

    @Query("SELECT * FROM maintenances WHERE car_id = :carId ORDER BY date DESC")
    suspend fun getListByCarId(carId: Long): List<MaintenanceEntity>

    @Query("SELECT * FROM maintenances WHERE car_id = :carId ORDER BY date DESC")
    fun getListByCarIdFlow(carId: Long): Flow<List<MaintenanceEntity>>

    @Query("SELECT * FROM maintenances WHERE car_id = :carId AND type = :type ORDER BY date DESC")
    suspend fun getListByCarIdAndType(carId: Long, type: String): List<MaintenanceEntity>

    @Query("""
        SELECT * FROM maintenances
        WHERE car_id = :carId
        AND id IN (
            SELECT id FROM maintenances m2
            WHERE m2.car_id = :carId
            GROUP BY m2.type
            HAVING m2.date = MAX(m2.date)
        )
        ORDER BY date DESC
    """)
    suspend fun getLatestByType(carId: Long): List<MaintenanceEntity>

    @Query("""
        SELECT * FROM maintenances
        WHERE car_id = :carId
        AND id IN (
            SELECT id FROM maintenances m2
            WHERE m2.car_id = :carId
            GROUP BY m2.type
            HAVING m2.date = MAX(m2.date)
        )
        ORDER BY date DESC
    """)
    fun getLatestByTypeFlow(carId: Long): Flow<List<MaintenanceEntity>>

    @Query("SELECT * FROM maintenances WHERE car_id = :carId ORDER BY date DESC LIMIT :limit")
    suspend fun getRecentList(carId: Long, limit: Int): List<MaintenanceEntity>

    @Query("SELECT * FROM maintenances WHERE car_id = :carId ORDER BY date DESC LIMIT :limit")
    fun getRecentListFlow(carId: Long, limit: Int): Flow<List<MaintenanceEntity>>

    @Query("SELECT * FROM maintenances ORDER BY date DESC")
    suspend fun getAll(): List<MaintenanceEntity>

    @Query("DELETE FROM maintenances")
    suspend fun deleteAll()
}
