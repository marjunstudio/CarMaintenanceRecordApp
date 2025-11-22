package to.msn.wings.carmaintenancerecord.ui.navigation

/**
 * アプリ内の画面定義
 */
sealed class Screen(val route: String) {
    /**
     * ホーム画面（未実装）
     */
    data object Home : Screen("home")

    /**
     * 車両管理画面
     */
    data object Car : Screen("car")

    /**
     * メンテナンス履歴一覧画面
     * @param carId 車両ID
     */
    data object MaintenanceList : Screen("maintenance_list/{carId}") {
        fun createRoute(carId: Long): String = "maintenance_list/$carId"
    }

    /**
     * メンテナンス詳細・登録画面
     * @param carId 車両ID
     * @param maintenanceId メンテナンス記録ID（新規作成の場合は0）
     */
    data object MaintenanceDetail : Screen("maintenance_detail/{carId}?maintenanceId={maintenanceId}") {
        fun createRoute(carId: Long, maintenanceId: Long = 0L): String {
            return if (maintenanceId > 0L) {
                "maintenance_detail/$carId?maintenanceId=$maintenanceId"
            } else {
                "maintenance_detail/$carId"
            }
        }
    }
}
