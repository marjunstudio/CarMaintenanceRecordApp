package to.msn.wings.carmaintenancerecord.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import to.msn.wings.carmaintenancerecord.data.local.AppDatabase
import to.msn.wings.carmaintenancerecord.data.local.dao.CarDao
import to.msn.wings.carmaintenancerecord.data.local.dao.MaintenanceDao
import to.msn.wings.carmaintenancerecord.data.local.dao.MaintenanceIntervalSettingDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "car_maintenance_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCarDao(database: AppDatabase): CarDao {
        return database.carDao()
    }

    @Provides
    @Singleton
    fun provideMaintenanceDao(database: AppDatabase): MaintenanceDao {
        return database.maintenanceDao()
    }

    @Provides
    @Singleton
    fun provideMaintenanceIntervalSettingDao(database: AppDatabase): MaintenanceIntervalSettingDao {
        return database.maintenanceIntervalSettingDao()
    }
}
