package to.msn.wings.carmaintenancerecord.domain.model

/**
 * メンテナンス周期設定を表すドメインモデル
 *
 * ユーザーが各メンテナンス項目の実施周期をカスタマイズするための設定。
 * 距離ベース・日数ベースのいずれか、または両方を設定可能。
 *
 * @property id 設定ID（0の場合は新規作成）
 * @property carId 車両ID
 * @property type メンテナンス種別
 * @property intervalKm 距離ベースの周期（km）。nullの場合は距離ベースの通知を行わない
 * @property intervalDays 日数ベースの周期（日）。nullの場合は日数ベースの通知を行わない
 */
data class MaintenanceIntervalSetting(
    val id: Long = 0L,
    val carId: Long,
    val type: MaintenanceType,
    val intervalKm: Int?,
    val intervalDays: Int?
) {
    /**
     * 距離ベースの設定が有効かどうか
     */
    val hasDistanceSetting: Boolean
        get() = intervalKm != null && intervalKm > 0

    /**
     * 日数ベースの設定が有効かどうか
     */
    val hasDaysSetting: Boolean
        get() = intervalDays != null && intervalDays > 0

    /**
     * 設定が全く存在しないか（両方null）
     */
    val isEmpty: Boolean
        get() = !hasDistanceSetting && !hasDaysSetting

    /**
     * 両方の設定があるか
     */
    val hasBothSettings: Boolean
        get() = hasDistanceSetting && hasDaysSetting

    companion object {
        /**
         * デフォルト設定を生成（MaintenanceTypeの推奨値を使用）
         */
        fun createDefault(carId: Long, type: MaintenanceType): MaintenanceIntervalSetting {
            return MaintenanceIntervalSetting(
                carId = carId,
                type = type,
                intervalKm = type.defaultIntervalKm,
                intervalDays = type.defaultIntervalDays
            )
        }
    }
}
