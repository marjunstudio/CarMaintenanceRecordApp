package to.msn.wings.carmaintenancerecord.data.mapper

import to.msn.wings.carmaintenancerecord.data.local.entity.MaintenanceIntervalSettingEntity
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceIntervalSetting
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType

fun MaintenanceIntervalSettingEntity.toDomain(): MaintenanceIntervalSetting {
    return MaintenanceIntervalSetting(
        id = id,
        carId = carId,
        type = MaintenanceType.valueOf(type),
        intervalKm = intervalKm,
        intervalDays = intervalDays
    )
}

fun MaintenanceIntervalSetting.toEntity(): MaintenanceIntervalSettingEntity {
    return MaintenanceIntervalSettingEntity(
        id = if (id == 0L) 0L else id,
        carId = carId,
        type = type.name,
        intervalKm = intervalKm,
        intervalDays = intervalDays
    )
}

fun List<MaintenanceIntervalSettingEntity>.toDomainList(): List<MaintenanceIntervalSetting> {
    return map { it.toDomain() }
}
