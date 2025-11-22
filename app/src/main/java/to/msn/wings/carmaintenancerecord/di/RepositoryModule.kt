package to.msn.wings.carmaintenancerecord.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import to.msn.wings.carmaintenancerecord.data.repository.CarRepository
import to.msn.wings.carmaintenancerecord.data.repository.CarRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCarRepository(
        carRepositoryImpl: CarRepositoryImpl
    ): CarRepository
}
