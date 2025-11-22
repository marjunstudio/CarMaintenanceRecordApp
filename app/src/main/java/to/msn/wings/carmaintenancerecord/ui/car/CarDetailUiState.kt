package to.msn.wings.carmaintenancerecord.ui.car

import to.msn.wings.carmaintenancerecord.domain.model.Car
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType

/**
 * 車両詳細画面のUI状態
 *
 * @property car 表示する車両情報
 * @property recentMaintenanceList 直近のメンテナンス履歴（最大3件）
 * @property nextMaintenance 次回メンテナンス情報
 * @property isLoading ローディング状態
 * @property errorMessage エラーメッセージ
 */
data class CarDetailUiState(
    val car: Car? = null,
    val recentMaintenanceList: List<Maintenance> = emptyList(),
    val nextMaintenance: NextMaintenance? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * 次回メンテナンス情報
 *
 * @property type メンテナンスタイプ
 * @property remainingDistance 残り距離（km）
 * @property progressPercentage 進捗率（0.0〜1.0）
 */
data class NextMaintenance(
    val type: MaintenanceType,
    val remainingDistance: Int,
    val progressPercentage: Float
) {
    fun getDisplayText(): String {
        return when {
            remainingDistance <= 0 -> "実施時期です"
            remainingDistance < 1000 -> "残り ${remainingDistance} km"
            else -> "残り ${String.format("%.1f", remainingDistance / 1000.0)} km"
        }
    }
}
