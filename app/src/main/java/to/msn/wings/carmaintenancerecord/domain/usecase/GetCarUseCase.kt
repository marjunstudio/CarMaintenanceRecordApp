package to.msn.wings.carmaintenancerecord.domain.usecase

import kotlinx.coroutines.flow.Flow
import to.msn.wings.carmaintenancerecord.data.repository.CarRepository
import to.msn.wings.carmaintenancerecord.domain.model.Car
import javax.inject.Inject

class GetCarUseCase @Inject constructor(
    private val carRepository: CarRepository
) {
    operator fun invoke(): Flow<Car?> {
        return carRepository.getFirstCarFlow()
    }
}
