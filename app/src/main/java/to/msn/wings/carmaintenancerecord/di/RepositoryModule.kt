package to.msn.wings.carmaintenancerecord.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import to.msn.wings.carmaintenancerecord.data.repository.CarRepository
import to.msn.wings.carmaintenancerecord.data.repository.CarRepositoryImpl
import to.msn.wings.carmaintenancerecord.data.repository.MaintenanceIntervalSettingRepository
import to.msn.wings.carmaintenancerecord.data.repository.MaintenanceIntervalSettingRepositoryImpl
import to.msn.wings.carmaintenancerecord.data.repository.MaintenanceRepository
import to.msn.wings.carmaintenancerecord.data.repository.MaintenanceRepositoryImpl
import to.msn.wings.carmaintenancerecord.data.repository.UserPreferencesRepository
import to.msn.wings.carmaintenancerecord.data.repository.UserPreferencesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCarRepository(
        carRepositoryImpl: CarRepositoryImpl
    ): CarRepository

    @Binds
    @Singleton
    abstract fun bindMaintenanceRepository(
        maintenanceRepositoryImpl: MaintenanceRepositoryImpl
    ): MaintenanceRepository

    @Binds
    @Singleton
    abstract fun bindMaintenanceIntervalSettingRepository(
        maintenanceIntervalSettingRepositoryImpl: MaintenanceIntervalSettingRepositoryImpl
    ): MaintenanceIntervalSettingRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}
