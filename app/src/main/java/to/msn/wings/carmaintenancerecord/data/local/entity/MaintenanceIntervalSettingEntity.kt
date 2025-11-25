package to.msn.wings.carmaintenancerecord.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * メンテナンス周期設定テーブルのエンティティ
 *
 * ユーザーが設定したメンテナンス項目ごとの実施周期を保存する。
 * 車両が削除されたら関連する設定も削除される（CASCADE）。
 */
@Entity(
    tableName = "maintenance_interval_settings",
    foreignKeys = [
        ForeignKey(
            entity = CarEntity::class,
            parentColumns = ["id"],
            childColumns = ["car_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["car_id"]),
        Index(value = ["car_id", "type"], unique = true)
    ]
)
data class MaintenanceIntervalSettingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "car_id")
    val carId: Long,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "interval_km")
    val intervalKm: Int?,

    @ColumnInfo(name = "interval_days")
    val intervalDays: Int?
)
