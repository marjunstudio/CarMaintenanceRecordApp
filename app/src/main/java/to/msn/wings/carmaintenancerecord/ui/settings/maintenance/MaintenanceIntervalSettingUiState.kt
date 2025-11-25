package to.msn.wings.carmaintenancerecord.ui.settings.maintenance

import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType


data class MaintenanceIntervalSettingUiState(
    val carId: Long? = null,
    val settingItems: List<SettingItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * 各メンテナンス項目の設定情報
 *
 * @property type メンテナンス種別
 * @property customIntervalKm ユーザー設定の距離周期（km）。nullの場合はデフォルト値を使用
 * @property customIntervalDays ユーザー設定の日数周期（日）。nullの場合はデフォルト値を使用
 * @property defaultIntervalKm デフォルトの距離周期（km）
 * @property defaultIntervalDays デフォルトの日数周期（日）
 */
data class SettingItem(
    val type: MaintenanceType,
    val customIntervalKm: Int?,
    val customIntervalDays: Int?,
    val defaultIntervalKm: Int?,
    val defaultIntervalDays: Int?
) {
    /**
     * 表示用の現在値テキスト
     */
    val currentValueText: String
        get() {
            val actualKm = customIntervalKm ?: defaultIntervalKm
            val actualDays = customIntervalDays ?: defaultIntervalDays

            return when {
                actualKm != null && actualDays != null -> {
                    "${formatDistance(actualKm)} または ${formatDays(actualDays)}"
                }
                actualKm != null -> formatDistance(actualKm)
                actualDays != null -> formatDays(actualDays)
                else -> "未設定"
            }
        }

    /**
     * カスタム設定があるかどうか
     */
    val isCustomized: Boolean
        get() = customIntervalKm != null || customIntervalDays != null

    private fun formatDistance(km: Int): String {
        return when {
            km >= 10000 -> String.format("%.1f万km", km / 10000.0)
            km >= 1000 -> String.format("%.1f千km", km / 1000.0)
            else -> "${km}km"
        }
    }

    private fun formatDays(days: Int): String {
        return when {
            days >= 365 -> {
                val years = days / 365.0
                if (years == years.toInt().toDouble()) {
                    "${years.toInt()}年"
                } else {
                    String.format("%.1f年", years)
                }
            }
            days >= 30 -> {
                val months = days / 30.0
                if (months == months.toInt().toDouble()) {
                    "${months.toInt()}ヶ月"
                } else {
                    String.format("%.1fヶ月", months)
                }
            }
            else -> "${days}日"
        }
    }
}
