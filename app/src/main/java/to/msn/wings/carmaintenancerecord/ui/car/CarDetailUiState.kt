package to.msn.wings.carmaintenancerecord.ui.car

import to.msn.wings.carmaintenancerecord.domain.model.Car
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType

/**
 * 車両詳細画面のUI状態
 *
 * @property car 表示する車両情報
 * @property recentMaintenanceList 直近のメンテナンス履歴（最大3件）
 * @property nextMaintenanceList 次回メンテナンス情報のリスト（複数表示対応）
 * @property isLoading ローディング状態
 * @property errorMessage エラーメッセージ
 */
data class CarDetailUiState(
    val car: Car? = null,
    val recentMaintenanceList: List<Maintenance> = emptyList(),
    val nextMaintenanceList: List<NextMaintenance> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * 次回メンテナンス情報
 *
 * @property type メンテナンスタイプ
 * @property remainingDistance 残り距離（km）、距離ベースでない場合はnull
 * @property remainingDays 残り日数、日数ベースでない場合はnull
 * @property progressPercentage 進捗率（0.0〜1.0）
 */
data class NextMaintenance(
    val type: MaintenanceType,
    val remainingDistance: Int? = null,
    val remainingDays: Int? = null,
    val progressPercentage: Float
) {
    /**
     * 次回メンテナンスまでの表示テキストを取得
     */
    fun getDisplayText(): String {
        return when {
            remainingDistance != null -> {
                when {
                    remainingDistance <= 0 -> "実施時期です"
                    remainingDistance < 1000 -> "残り $remainingDistance km"
                    else -> "残り $remainingDistance km"
                }
            }
            remainingDays != null -> {
                when {
                    remainingDays <= 0 -> "実施時期です"
                    remainingDays < 30 -> "残り $remainingDays 日"
                    remainingDays < 365 -> "残り ${remainingDays / 30} ヶ月"
                    else -> "残り ${String.format("%.1f", remainingDays / 365.0)} 年"
                }
            }
            else -> "期限なし"
        }
    }

    /**
     * 緊急度を取得（UI表示での色分けに使用）
     * @return HIGH: 赤、MEDIUM: 黄、LOW: 緑
     */
    fun getUrgency(): Urgency {
        val progress = progressPercentage
        return when {
            progress >= 1.0f -> Urgency.HIGH
            progress >= 0.7f -> Urgency.MEDIUM
            else -> Urgency.LOW
        }
    }
}

/**
 * メンテナンスの緊急度
 */
enum class Urgency {
    LOW,    // 余裕あり
    MEDIUM, // そろそろ
    HIGH    // 至急
}
