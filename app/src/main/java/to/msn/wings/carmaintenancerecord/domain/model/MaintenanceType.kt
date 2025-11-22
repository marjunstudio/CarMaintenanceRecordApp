package to.msn.wings.carmaintenancerecord.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * メンテナンス種別を表すEnum
 *
 * @property displayName 表示用の日本語名
 * @property defaultIntervalKm デフォルトのメンテナンス間隔（km単位）、null の場合は距離ベースではない
 * @property defaultIntervalDays デフォルトのメンテナンス間隔（日数単位）、null の場合は日数ベースではない
 * @property icon 表示用のアイコン
 */
enum class MaintenanceType(
    val displayName: String,
    val defaultIntervalKm: Int?,
    val defaultIntervalDays: Int?,
    val icon: ImageVector
) {
    /**
     * オイル交換
     * 推奨: 5,000km ごと
     */
    OIL_CHANGE(
        displayName = "オイル交換",
        defaultIntervalKm = 5000,
        defaultIntervalDays = null,
        icon = Icons.Default.Build
    ),

    /**
     * オイルフィルター交換
     * 推奨: 10,000km ごと
     */
    OIL_FILTER_CHANGE(
        displayName = "オイルフィルター交換",
        defaultIntervalKm = 10000,
        defaultIntervalDays = null,
        icon = Icons.Default.FilterAlt
    ),

    /**
     * タイヤ交換
     * 推奨: 40,000km ごと
     */
    TIRE_CHANGE(
        displayName = "タイヤ交換",
        defaultIntervalKm = 40000,
        defaultIntervalDays = null,
        icon = Icons.Default.DirectionsCar
    ),

    /**
     * タイヤローテーション
     * 推奨: 10,000km ごと
     */
    TIRE_ROTATION(
        displayName = "タイヤローテーション",
        defaultIntervalKm = 10000,
        defaultIntervalDays = null,
        icon = Icons.Default.Cached
    ),

    /**
     * ブレーキパッド交換
     * 推奨: 30,000km ごと
     */
    BRAKE_PAD_CHANGE(
        displayName = "ブレーキパッド交換",
        defaultIntervalKm = 30000,
        defaultIntervalDays = null,
        icon = Icons.Default.Warning
    ),

    /**
     * バッテリー交換
     * 推奨: 3年（1,095日）ごと
     */
    BATTERY_CHANGE(
        displayName = "バッテリー交換",
        defaultIntervalKm = null,
        defaultIntervalDays = 1095,
        icon = Icons.Default.BatteryChargingFull
    ),

    /**
     * エアコンフィルター交換
     * 推奨: 1年（365日）ごと
     */
    AIR_FILTER_CHANGE(
        displayName = "エアコンフィルター交換",
        defaultIntervalKm = null,
        defaultIntervalDays = 365,
        icon = Icons.Default.Air
    ),

    /**
     * 車検
     * 推奨: 2年（730日）ごと
     */
    VEHICLE_INSPECTION(
        displayName = "車検",
        defaultIntervalKm = null,
        defaultIntervalDays = 730,
        icon = Icons.Default.FactCheck
    ),

    /**
     * 法定12ヶ月点検
     * 推奨: 1年（365日）ごと
     */
    LEGAL_INSPECTION(
        displayName = "法定12ヶ月点検",
        defaultIntervalKm = null,
        defaultIntervalDays = 365,
        icon = Icons.Default.Assignment
    ),

    /**
     * ワイパー交換
     * 推奨: 1年（365日）ごと
     */
    WIPER_CHANGE(
        displayName = "ワイパー交換",
        defaultIntervalKm = null,
        defaultIntervalDays = 365,
        icon = Icons.Default.CleaningServices
    ),

    /**
     * その他のメンテナンス
     * 推奨間隔なし
     */
    OTHER(
        displayName = "その他",
        defaultIntervalKm = null,
        defaultIntervalDays = null,
        icon = Icons.Default.MoreHoriz
    );

    companion object {
        /**
         * 表示名から MaintenanceType を取得する
         * @param displayName 表示名
         * @return 対応する MaintenanceType、見つからない場合は OTHER
         */
        fun fromDisplayName(displayName: String): MaintenanceType {
            return values().find { it.displayName == displayName } ?: OTHER
        }
    }
}
