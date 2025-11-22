package to.msn.wings.carmaintenancerecord

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import to.msn.wings.carmaintenancerecord.ui.car.CarScreen
import to.msn.wings.carmaintenancerecord.ui.theme.CarMaintenanceRecordTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarMaintenanceRecordTheme {
                CarScreen()
            }
        }
    }
}