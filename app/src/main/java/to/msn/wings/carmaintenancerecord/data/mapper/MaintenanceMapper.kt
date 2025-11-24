package to.msn.wings.carmaintenancerecord.data.mapper

import to.msn.wings.carmaintenancerecord.data.local.entity.MaintenanceEntity
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType

fun MaintenanceEntity.toDomain(): Maintenance {
    return Maintenance(
        id = id,
        carId = carId,
        type = MaintenanceType.valueOf(type),
        date = date,
        mileage = mileage,
        memo = memo,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Maintenance.toEntity(): MaintenanceEntity {
    return MaintenanceEntity(
        id = id,
        carId = carId,
        type = type.name,
        date = date,
        mileage = mileage,
        memo = memo,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun List<MaintenanceEntity>.toDomainList(): List<Maintenance> {
    return map { it.toDomain() }
}

fun List<Maintenance>.toEntityList(): List<MaintenanceEntity> {
    return map { it.toEntity() }
}
