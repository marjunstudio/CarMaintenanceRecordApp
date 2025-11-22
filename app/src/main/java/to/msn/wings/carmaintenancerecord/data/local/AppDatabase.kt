package to.msn.wings.carmaintenancerecord.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import to.msn.wings.carmaintenancerecord.data.local.dao.CarDao
import to.msn.wings.carmaintenancerecord.data.local.entity.CarEntity

@Database(
    entities = [CarEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
}
