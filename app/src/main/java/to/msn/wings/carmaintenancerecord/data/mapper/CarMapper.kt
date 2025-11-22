package to.msn.wings.carmaintenancerecord.data.mapper

import to.msn.wings.carmaintenancerecord.data.local.entity.CarEntity
import to.msn.wings.carmaintenancerecord.domain.model.Car

fun CarEntity.toDomain(): Car {
    return Car(
        id = id,
        name = name,
        manufacturer = manufacturer,
        model = model,
        year = year,
        licensePlate = licensePlate,
        initialMileage = initialMileage,
        mileage = mileage,
        photoUri = photoUri,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Car.toEntity(): CarEntity {
    return CarEntity(
        id = id,
        name = name,
        manufacturer = manufacturer,
        model = model,
        year = year,
        licensePlate = licensePlate,
        initialMileage = initialMileage,
        mileage = mileage,
        photoUri = photoUri,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
