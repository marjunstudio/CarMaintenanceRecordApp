package to.msn.wings.carmaintenancerecord.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * メンテナンス記録のRoomエンティティ
 *
 * メンテナンス履歴をデータベースに保存するためのエンティティクラス。
 * CarEntityに対する外部キー制約を持ち、車両が削除されるとメンテナンス記録も削除されます（CASCADE）。
 *
 * @property id メンテナンス記録ID（自動採番）
 * @property carId 対象車両のID（外部キー）
 * @property type メンテナンス種別（MaintenanceTypeのname）
 * @property date 実施日時（Unixタイムスタンプ、ミリ秒）
 * @property mileage 実施時の走行距離（km）
 * @property cost 費用（円）、未入力の場合は null
 * @property shop 実施店舗名、未入力の場合は null
 * @property memo メモ、未入力の場合は null
 * @property createdAt 作成日時（Unixタイムスタンプ、ミリ秒）
 * @property updatedAt 更新日時（Unixタイムスタンプ、ミリ秒）
 */
@Entity(
    tableName = "maintenances",
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
        Index(value = ["date"])
    ]
)
data class MaintenanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "car_id")
    val carId: Long,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "mileage")
    val mileage: Int,

    @ColumnInfo(name = "cost")
    val cost: Int?,

    @ColumnInfo(name = "shop")
    val shop: String?,

    @ColumnInfo(name = "memo")
    val memo: String?,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
