package to.msn.wings.carmaintenancerecord.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import to.msn.wings.carmaintenancerecord.ui.main.MainScreen

/**
 * アプリのNavigation Graph
 *
 * @param navController NavHostController
 * @param modifier Modifier
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = modifier
    ) {
        // メイン画面（タブナビゲーション）
        composable(route = Screen.Main.route) {
            MainScreen()
        }
    }
}
