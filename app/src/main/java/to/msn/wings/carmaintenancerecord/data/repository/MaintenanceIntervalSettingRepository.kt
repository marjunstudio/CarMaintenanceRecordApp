package to.msn.wings.carmaintenancerecord.data.repository

import kotlinx.coroutines.flow.Flow
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceIntervalSetting
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType


interface MaintenanceIntervalSettingRepository {

    /**
     * 指定車両のすべての設定を取得（Flow）
     */
    fun getSettingsByCarIdFlow(carId: Long): Flow<List<MaintenanceIntervalSetting>>

    /**
     * 指定車両のすべての設定を取得
     */
    suspend fun getSettingsByCarId(carId: Long): List<MaintenanceIntervalSetting>

    /**
     * 指定車両・種別の設定を取得
     */
    suspend fun getSettingByCarIdAndType(carId: Long, type: MaintenanceType): MaintenanceIntervalSetting?

    /**
     * 設定を保存
     */
    suspend fun saveSetting(setting: MaintenanceIntervalSetting): Long

    /**
     * 指定車両・種別の設定を削除（デフォルトに戻す）
     */
    suspend fun deleteSetting(carId: Long, type: MaintenanceType)

    /**
     * 指定車両のすべての設定を削除
     */
    suspend fun deleteAllSettings(carId: Long)
}
