package to.msn.wings.carmaintenancerecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import to.msn.wings.carmaintenancerecord.ui.navigation.AppNavGraph
import to.msn.wings.carmaintenancerecord.ui.theme.CarMaintenanceRecordTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarMaintenanceRecordTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}