package to.msn.wings.carmaintenancerecord.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import to.msn.wings.carmaintenancerecord.data.local.dao.CarDao
import to.msn.wings.carmaintenancerecord.data.mapper.toDomain
import to.msn.wings.carmaintenancerecord.data.mapper.toEntity
import to.msn.wings.carmaintenancerecord.domain.model.Car
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val carDao: CarDao
) : CarRepository {

    override suspend fun saveCar(car: Car): Long {
        return carDao.insert(car.toEntity())
    }

    override suspend fun updateCar(car: Car) {
        carDao.update(car.toEntity())
    }

    override suspend fun getCar(carId: Long): Car? {
        return carDao.getCar(carId)?.toDomain()
    }

    override fun getCarFlow(carId: Long): Flow<Car?> {
        return carDao.getCarFlow(carId).map { it?.toDomain() }
    }

    override suspend fun getFirstCar(): Car? {
        return carDao.getFirstCar()?.toDomain()
    }

    override fun getFirstCarFlow(): Flow<Car?> {
        return carDao.getFirstCarFlow().map { it?.toDomain() }
    }

    override suspend fun deleteCar(carId: Long) {
        carDao.deleteCar(carId)
    }
}
