package to.msn.wings.carmaintenancerecord.domain.usecase

import to.msn.wings.carmaintenancerecord.data.repository.CarRepository
import to.msn.wings.carmaintenancerecord.domain.model.Car
import javax.inject.Inject

class UpdateMileageUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    suspend operator fun invoke(car: Car, newMileage: Int) {
        val updatedCar = car.copy(
            mileage = newMileage,
            updatedAt = System.currentTimeMillis()
        )
        carRepository.updateCar(updatedCar)
    }
}
