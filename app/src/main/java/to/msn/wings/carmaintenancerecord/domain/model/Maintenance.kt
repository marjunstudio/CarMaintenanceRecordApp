package to.msn.wings.carmaintenancerecord.domain.model

/**
 * メンテナンス記録を表すドメインモデル
 *
 * 車両に対して実施したメンテナンスの履歴を管理。
 * オイル交換、タイヤ交換、車検などの各種メンテナンス情報を記録。
 *
 * @property id メンテナンス記録ID（0の場合は新規作成）
 * @property carId 対象車両のID
 * @property type メンテナンスの種別
 * @property date 実施日時（Unixタイムスタンプ、ミリ秒）
 * @property mileage 実施時の走行距離（km）
 * @property memo メモ、未入力の場合は null
 * @property createdAt 作成日時（Unixタイムスタンプ、ミリ秒）
 * @property updatedAt 更新日時（Unixタイムスタンプ、ミリ秒）
 */
data class Maintenance(
    val id: Long = 0L,
    val carId: Long,
    val type: MaintenanceType,
    val date: Long,
    val mileage: Int,
    val memo: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun isNew(): Boolean = id == 0L

    fun hasMemo(): Boolean = !memo.isNullOrBlank()
}
