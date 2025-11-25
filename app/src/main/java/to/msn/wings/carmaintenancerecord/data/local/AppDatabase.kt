package to.msn.wings.carmaintenancerecord.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import to.msn.wings.carmaintenancerecord.data.local.dao.CarDao
import to.msn.wings.carmaintenancerecord.data.local.dao.MaintenanceDao
import to.msn.wings.carmaintenancerecord.data.local.dao.MaintenanceIntervalSettingDao
import to.msn.wings.carmaintenancerecord.data.local.entity.CarEntity
import to.msn.wings.carmaintenancerecord.data.local.entity.MaintenanceEntity
import to.msn.wings.carmaintenancerecord.data.local.entity.MaintenanceIntervalSettingEntity

@Database(
    entities = [
        CarEntity::class,
        MaintenanceEntity::class,
        MaintenanceIntervalSettingEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun maintenanceDao(): MaintenanceDao
    abstract fun maintenanceIntervalSettingDao(): MaintenanceIntervalSettingDao
}
