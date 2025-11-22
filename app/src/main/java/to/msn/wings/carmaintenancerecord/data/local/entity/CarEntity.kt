package to.msn.wings.carmaintenancerecord.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "manufacturer")
    val manufacturer: String,

    @ColumnInfo(name = "model")
    val model: String,

    @ColumnInfo(name = "year")
    val year: Int,

    @ColumnInfo(name = "license_plate")
    val licensePlate: String? = null,

    @ColumnInfo(name = "initial_mileage")
    val initialMileage: Int = 0,

    @ColumnInfo(name = "mileage")
    val mileage: Int,

    @ColumnInfo(name = "photo_uri")
    val photoUri: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
