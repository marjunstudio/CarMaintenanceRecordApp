package to.msn.wings.carmaintenancerecord.ui.settings

/**
 * 設定画面のUI状態
 * @property appVersion アプリバージョン
 * @property isDarkMode ダークモードが有効かどうか
 */
data class SettingsUiState(
    val appVersion: String = "",
    val isDarkMode: Boolean = false
)
