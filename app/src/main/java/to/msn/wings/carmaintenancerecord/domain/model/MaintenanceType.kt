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
 * メンテナンス管理単位の種類
 */
enum class IntervalUnit {
    /** 距離（km）のみで管理 */
    DISTANCE_ONLY,
    /** 日数のみで管理 */
    DAYS_ONLY,
    /** 両方で管理可能 */
    BOTH,
    /** 管理単位なし（その他） */
    NONE
}

/**
 * メンテナンス種別を表すEnum
 *
 * @property displayName 表示用の日本語名
 * @property defaultIntervalKm デフォルトのメンテナンス間隔（km単位）、null の場合は距離ベースではない
 * @property defaultIntervalDays デフォルトのメンテナンス間隔（日数単位）、null の場合は日数ベースではない
 * @property intervalUnit 管理単位の種類
 * @property icon 表示用のアイコン
 */
enum class MaintenanceType(
    val displayName: String,
    val defaultIntervalKm: Int?,
    val defaultIntervalDays: Int?,
    val intervalUnit: IntervalUnit,
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
        intervalUnit = IntervalUnit.DISTANCE_ONLY,
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
        intervalUnit = IntervalUnit.DISTANCE_ONLY,
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
        intervalUnit = IntervalUnit.DISTANCE_ONLY,
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
        intervalUnit = IntervalUnit.DISTANCE_ONLY,
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
        intervalUnit = IntervalUnit.DISTANCE_ONLY,
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
        intervalUnit = IntervalUnit.DAYS_ONLY,
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
        intervalUnit = IntervalUnit.DAYS_ONLY,
        icon = Icons.Default.Air
    ),

    /**
     * ワイパー交換
     * 推奨: 1年（365日）ごと
     */
    WIPER_CHANGE(
        displayName = "ワイパー交換",
        defaultIntervalKm = null,
        defaultIntervalDays = 365,
        intervalUnit = IntervalUnit.DAYS_ONLY,
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
        intervalUnit = IntervalUnit.NONE,
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
