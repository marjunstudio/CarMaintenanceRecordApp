package to.msn.wings.carmaintenancerecord.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import to.msn.wings.carmaintenancerecord.ui.car.CarScreen
import to.msn.wings.carmaintenancerecord.ui.home.HomeScreen
import to.msn.wings.carmaintenancerecord.ui.maintenance.MaintenanceDetailScreen
import to.msn.wings.carmaintenancerecord.ui.maintenance.MaintenanceListScreen

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
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // ホーム画面
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToCar = {
                    navController.navigate(Screen.Car.route)
                },
                onNavigateToMaintenanceList = { carId ->
                    navController.navigate(Screen.MaintenanceList.createRoute(carId))
                }
            )
        }

        // 車両管理画面
        composable(route = Screen.Car.route) {
            CarScreen(
                onNavigateToMaintenanceList = { carId ->
                    navController.navigate(Screen.MaintenanceList.createRoute(carId))
                }
            )
        }

        // メンテナンス履歴一覧画面
        composable(
            route = Screen.MaintenanceList.route,
            arguments = listOf(
                navArgument("carId") {
                    type = NavType.LongType
                    defaultValue = 1L
                }
            )
        ) { backStackEntry ->
            val carId = backStackEntry.arguments?.getLong("carId") ?: 1L

            MaintenanceListScreen(
                onNavigateToDetail = { maintenanceId ->
                    navController.navigate(
                        Screen.MaintenanceDetail.createRoute(carId, maintenanceId)
                    )
                },
                onNavigateToAdd = {
                    navController.navigate(
                        Screen.MaintenanceDetail.createRoute(carId)
                    )
                }
            )
        }

        // メンテナンス詳細・登録画面
        composable(
            route = Screen.MaintenanceDetail.route,
            arguments = listOf(
                navArgument("carId") {
                    type = NavType.LongType
                    defaultValue = 1L
                },
                navArgument("maintenanceId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) {
            MaintenanceDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
