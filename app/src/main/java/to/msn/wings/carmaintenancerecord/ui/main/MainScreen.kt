package to.msn.wings.carmaintenancerecord.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import to.msn.wings.carmaintenancerecord.ui.car.CarDetailScreen
import to.msn.wings.carmaintenancerecord.ui.car.CarEditScreen
import to.msn.wings.carmaintenancerecord.ui.maintenance.MaintenanceDetailScreen
import to.msn.wings.carmaintenancerecord.ui.maintenance.MaintenanceListScreen
import to.msn.wings.carmaintenancerecord.ui.navigation.Screen
import to.msn.wings.carmaintenancerecord.ui.settings.SettingsScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem(
            route = Screen.CarDetail.route,
            label = "ガレージ",
            icon = Icons.Default.DirectionsCar
        ),
        BottomNavItem(
            route = "maintenance_list",
            label = "メンテナンス",
            icon = Icons.AutoMirrored.Filled.List
        ),
        BottomNavItem(
            route = Screen.Settings.route,
            label = "設定",
            icon = Icons.Default.Settings
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.route?.startsWith(item.route.split("/").first()) == true
                    } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (item.route == "maintenance_list") {
                                navController.navigate(Screen.MaintenanceList.createRoute(1L)) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            } else {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.CarDetail.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screen.CarDetail.route) {
                CarDetailScreen(
                    onNavigateToCarEdit = {
                        navController.navigate(Screen.CarEdit.route)
                    },
                    onNavigateToMaintenanceAdd = {
                        navController.navigate(Screen.MaintenanceDetail.createRoute(1L))
                    },
                    onNavigateToMaintenanceList = {
                        navController.navigate(Screen.MaintenanceList.createRoute(1L))
                    },
                    onNavigateToMileageUpdate = {
                        navController.navigate(Screen.CarEdit.route)
                    }
                )
            }

            composable(route = Screen.CarEdit.route) {
                CarEditScreen(
                    onNavigateToMaintenanceList = { carId ->
                        navController.navigate(Screen.MaintenanceList.createRoute(carId))
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

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

            composable(route = Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
