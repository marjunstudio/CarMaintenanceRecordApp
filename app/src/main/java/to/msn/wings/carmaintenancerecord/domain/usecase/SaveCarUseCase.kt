package to.msn.wings.carmaintenancerecord.domain.usecase

import to.msn.wings.carmaintenancerecord.data.repository.CarRepository
import to.msn.wings.carmaintenancerecord.domain.model.Car
import javax.inject.Inject

class SaveCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend operator fun invoke(car: Car): Long {
        val now = System.currentTimeMillis()
        val carToSave = car.copy(
            createdAt = now,
            updatedAt = now
        )
        return carRepository.saveCar(carToSave)
    }
}
