package to.msn.wings.carmaintenancerecord.ui.car

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import to.msn.wings.carmaintenancerecord.domain.model.Car
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import to.msn.wings.carmaintenancerecord.ui.theme.CarMaintenanceRecordTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CarDetailScreen(
    onNavigateToCarEdit: () -> Unit = {},
    onNavigateToMaintenanceAdd: () -> Unit = {},
    onNavigateToMaintenanceList: () -> Unit = {},
    onNavigateToMileageUpdate: () -> Unit = {},
    viewModel: CarDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    CarDetailScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNavigateToCarEdit = onNavigateToCarEdit,
        onNavigateToMaintenanceAdd = onNavigateToMaintenanceAdd,
        onNavigateToMaintenanceList = onNavigateToMaintenanceList,
        onNavigateToMileageUpdate = onNavigateToMileageUpdate
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarDetailScreenContent(
    uiState: CarDetailUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateToCarEdit: () -> Unit,
    onNavigateToMaintenanceAdd: () -> Unit,
    onNavigateToMaintenanceList: () -> Unit,
    onNavigateToMileageUpdate: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "マイガレージ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToCarEdit) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "設定"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.car == null) {
            EmptyCarView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onNavigateToCarEdit = onNavigateToCarEdit
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                VehicleOverviewCard(
                    car = uiState.car,
                    nextMaintenance = uiState.nextMaintenance,
                    modifier = Modifier.padding(16.dp)
                )

                ButtonGroup(
                    onMileageUpdateClick = onNavigateToMileageUpdate,
                    onAddRecordClick = onNavigateToMaintenanceAdd,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.recentMaintenanceList.isNotEmpty()) {
                    Text(
                        text = "直近のメンテナンス",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    RecentMaintenanceList(
                        maintenanceList = uiState.recentMaintenanceList,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = onNavigateToMaintenanceList,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "すべての整備記録を見る",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun EmptyCarView(
    modifier: Modifier = Modifier,
    onNavigateToCarEdit: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                modifier = Modifier.size(96.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "あなたの愛車を登録しよう",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "まずはじめに、メンテナンスを記録する車を追加してください。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        Button(
            onClick = onNavigateToCarEdit,
            modifier = Modifier
                .fillMaxWidth(0.8f),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 8.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("車を登録する")
        }
    }
}

@Composable
private fun VehicleOverviewCard(
    car: Car,
    nextMaintenance: NextMaintenance?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "写真を追加",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "プライマリー車両",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${car.manufacturer} ${car.model}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column {
                    Text(
                        text = "現在の走行距離",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${String.format("%,d", car.mileage)} km",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                nextMaintenance?.let { maintenance ->
                    Spacer(modifier = Modifier.height(16.dp))

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "次回${maintenance.type.displayName}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = maintenance.getDisplayText(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { maintenance.progressPercentage },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ButtonGroup(
    onMileageUpdateClick: () -> Unit,
    onAddRecordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FilledTonalButton(
            onClick = onMileageUpdateClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Speed,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "走行距離を更新",
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = onAddRecordClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "記録を追加",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RecentMaintenanceList(
    maintenanceList: List<Maintenance>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        maintenanceList.forEach { maintenance ->
            MaintenanceRecordItem(maintenance = maintenance)
        }
    }
}

@Composable
private fun MaintenanceRecordItem(
    maintenance: Maintenance,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = maintenance.type.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Column {
                    Text(
                        text = maintenance.type.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatDate(maintenance.date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "${String.format("%,d", maintenance.mileage)} km",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy年M月d日", Locale.JAPANESE)
    return sdf.format(Date(timestamp))
}

@Preview(name = "車両詳細画面", showBackground = true)
@Composable
private fun CarDetailScreenPreview() {
    CarMaintenanceRecordTheme {
        CarDetailScreenContent(
            uiState = CarDetailUiState(
                car = Car(
                    id = 1L,
                    name = "マイカー",
                    manufacturer = "トヨタ",
                    model = "プリウス",
                    year = 2020,
                    licensePlate = null,
                    initialMileage = 0,
                    mileage = 125800,
                    photoUri = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ),
                recentMaintenanceList = listOf(
                    Maintenance(
                        id = 1L,
                        carId = 1L,
                        type = MaintenanceType.OIL_CHANGE,
                        date = System.currentTimeMillis() - 86400000L * 30,
                        mileage = 124300,
                        cost = 5000,
                        shop = "オートバックス"
                    ),
                    Maintenance(
                        id = 2L,
                        carId = 1L,
                        type = MaintenanceType.TIRE_ROTATION,
                        date = System.currentTimeMillis() - 86400000L * 60,
                        mileage = 122500,
                        cost = 3000,
                        shop = null
                    ),
                    Maintenance(
                        id = 3L,
                        carId = 1L,
                        type = MaintenanceType.AIR_FILTER_CHANGE,
                        date = System.currentTimeMillis() - 86400000L * 90,
                        mileage = 120150,
                        cost = 2500,
                        shop = null
                    )
                ),
                nextMaintenance = NextMaintenance(
                    type = MaintenanceType.OIL_CHANGE,
                    remainingDistance = 1500,
                    progressPercentage = 0.85f
                ),
                isLoading = false,
                errorMessage = null
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onNavigateToCarEdit = {},
            onNavigateToMaintenanceAdd = {},
            onNavigateToMaintenanceList = {},
            onNavigateToMileageUpdate = {}
        )
    }
}

@Preview(name = "空状態", showBackground = true)
@Composable
private fun CarDetailScreenPreview_Empty() {
    CarMaintenanceRecordTheme {
        CarDetailScreenContent(
            uiState = CarDetailUiState(
                car = null,
                recentMaintenanceList = emptyList(),
                nextMaintenance = null,
                isLoading = false,
                errorMessage = null
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onNavigateToCarEdit = {},
            onNavigateToMaintenanceAdd = {},
            onNavigateToMaintenanceList = {},
            onNavigateToMileageUpdate = {}
        )
    }
}
