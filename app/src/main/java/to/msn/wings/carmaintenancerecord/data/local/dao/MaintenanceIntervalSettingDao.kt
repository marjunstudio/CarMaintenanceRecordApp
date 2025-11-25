package to.msn.wings.carmaintenancerecord.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import to.msn.wings.carmaintenancerecord.data.local.entity.MaintenanceIntervalSettingEntity

@Dao
interface MaintenanceIntervalSettingDao {

    /**
     * 指定車両のすべての設定を取得（Flow）
     */
    @Query("SELECT * FROM maintenance_interval_settings WHERE car_id = :carId")
    fun getSettingsByCarIdFlow(carId: Long): Flow<List<MaintenanceIntervalSettingEntity>>

    /**
     * 指定車両のすべての設定を取得
     */
    @Query("SELECT * FROM maintenance_interval_settings WHERE car_id = :carId")
    suspend fun getSettingsByCarId(carId: Long): List<MaintenanceIntervalSettingEntity>

    /**
     * 指定車両・種別の設定を取得
     */
    @Query("SELECT * FROM maintenance_interval_settings WHERE car_id = :carId AND type = :type LIMIT 1")
    suspend fun getSettingByCarIdAndType(carId: Long, type: String): MaintenanceIntervalSettingEntity?

    /**
     * 設定を挿入または更新（REPLACE戦略）
     * car_id + type のユニーク制約により、同じ設定があれば上書きされる
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(setting: MaintenanceIntervalSettingEntity): Long

    /**
     * 指定車両・種別の設定を削除
     */
    @Query("DELETE FROM maintenance_interval_settings WHERE car_id = :carId AND type = :type")
    suspend fun deleteByCarIdAndType(carId: Long, type: String)

    /**
     * 指定車両のすべての設定を削除
     */
    @Query("DELETE FROM maintenance_interval_settings WHERE car_id = :carId")
    suspend fun deleteByCarId(carId: Long)
}
