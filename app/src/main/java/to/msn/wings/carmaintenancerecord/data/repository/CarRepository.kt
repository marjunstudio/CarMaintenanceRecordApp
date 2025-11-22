package to.msn.wings.carmaintenancerecord.data.repository

import kotlinx.coroutines.flow.Flow
import to.msn.wings.carmaintenancerecord.domain.model.Car

interface CarRepository {
    suspend fun saveCar(car: Car): Long
    suspend fun updateCar(car: Car)
    suspend fun getCar(carId: Long): Car?
    fun getCarFlow(carId: Long): Flow<Car?>
    suspend fun getFirstCar(): Car?
    fun getFirstCarFlow(): Flow<Car?>
    suspend fun deleteCar(carId: Long)
}
