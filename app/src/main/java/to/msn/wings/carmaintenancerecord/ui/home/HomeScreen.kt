package to.msn.wings.carmaintenancerecord.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import to.msn.wings.carmaintenancerecord.domain.model.Car
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import to.msn.wings.carmaintenancerecord.ui.theme.CarMaintenanceRecordTheme
import to.msn.wings.carmaintenancerecord.util.formatTimestamp
import java.util.Locale

@Composable
fun HomeScreen(
    onNavigateToCar: () -> Unit = {},
    onNavigateToMaintenanceList: (Long) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    HomeScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNavigateToCar = onNavigateToCar,
        onNavigateToMaintenanceList = {
            uiState.car?.let { car ->
                onNavigateToMaintenanceList(car.id)
            }
        }
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateToCar: () -> Unit,
    onNavigateToMaintenanceList: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ホーム",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.car == null) {
                NoCarRegisteredContent(onNavigateToCar = onNavigateToCar)
            } else {
                CarInfoCard(car = uiState.car)

                Spacer(modifier = Modifier.height(24.dp))

                RecentMaintenancesCard(
                    maintenances = uiState.recentMaintenances,
                    onNavigateToMaintenanceList = onNavigateToMaintenanceList
                )

                Spacer(modifier = Modifier.height(24.dp))

                NavigationButtons(
                    onNavigateToCar = onNavigateToCar,
                    onNavigateToMaintenanceList = onNavigateToMaintenanceList
                )
            }
        }
    }
}

@Composable
private fun NoCarRegisteredContent(onNavigateToCar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
            onClick = onNavigateToCar,
            modifier = Modifier
                .fillMaxWidth(0.8f),
            elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(
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
private fun CarInfoCard(car: Car) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "車両情報",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            InfoRow(label = "車名", value = car.name)
            InfoRow(label = "メーカー", value = car.manufacturer)
            InfoRow(label = "モデル", value = car.model)
            InfoRow(label = "年式", value = "${car.year}年")
            InfoRow(label = "現在の走行距離", value = "${String.format(Locale.getDefault(), "%,d", car.mileage)} km")
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun RecentMaintenancesCard(
    maintenances: List<Maintenance>,
    onNavigateToMaintenanceList: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "最近のメンテナンス",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (maintenances.isNotEmpty()) {
                    OutlinedButton(onClick = onNavigateToMaintenanceList) {
                        Text("すべて見る")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (maintenances.isEmpty()) {
                Text(
                    text = "まだメンテナンス記録がありません",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                maintenances.forEachIndexed { index, maintenance ->
                    if (index > 0) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                    MaintenanceItem(maintenance = maintenance)
                }
            }
        }
    }
}

@Composable
private fun MaintenanceItem(maintenance: Maintenance) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = maintenance.type.displayName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = formatTimestamp(maintenance.date, "yyyy/MM/dd"),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "${String.format(Locale.getDefault(), "%,d", maintenance.mileage)} km",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun NavigationButtons(
    onNavigateToCar: () -> Unit,
    onNavigateToMaintenanceList: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = onNavigateToCar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("車両情報を確認・更新")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onNavigateToMaintenanceList,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text("メンテナンス履歴を見る")
        }
    }
}

@Preview(name = "車両未登録", showBackground = true)
@Composable
private fun HomeScreenPreview_NoCar() {
    CarMaintenanceRecordTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                car = null,
                recentMaintenances = emptyList(),
                isLoading = false,
                errorMessage = null
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onNavigateToCar = {},
            onNavigateToMaintenanceList = {}
        )
    }
}

@Preview(name = "車両登録済み（メンテナンス記録なし）", showBackground = true)
@Composable
private fun HomeScreenPreview_WithCarNoMaintenance() {
    CarMaintenanceRecordTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                car = Car(
                    id = 1L,
                    name = "マイカー",
                    manufacturer = "トヨタ",
                    model = "プリウス",
                    year = 2020,
                    licensePlate = "品川 123 あ 4567",
                    initialMileage = 0,
                    mileage = 15000,
                    photoUri = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ),
                recentMaintenances = emptyList(),
                isLoading = false,
                errorMessage = null
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onNavigateToCar = {},
            onNavigateToMaintenanceList = {}
        )
    }
}

@Preview(name = "車両登録済み（メンテナンス記録あり）", showBackground = true)
@Composable
private fun HomeScreenPreview_WithCarAndMaintenance() {
    CarMaintenanceRecordTheme {
        HomeScreenContent(
            uiState = HomeUiState(
                car = Car(
                    id = 1L,
                    name = "マイカー",
                    manufacturer = "トヨタ",
                    model = "プリウス",
                    year = 2020,
                    licensePlate = "品川 123 あ 4567",
                    initialMileage = 0,
                    mileage = 15000,
                    photoUri = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ),
                recentMaintenances = listOf(
                    Maintenance(
                        id = 1L,
                        carId = 1L,
                        type = MaintenanceType.OIL_CHANGE,
                        date = System.currentTimeMillis() - 86400000L * 10,
                        mileage = 14500,
                        cost = 5000,
                        shop = "カーショップA",
                        memo = "オイル交換実施"
                    ),
                    Maintenance(
                        id = 2L,
                        carId = 1L,
                        type = MaintenanceType.TIRE_ROTATION,
                        date = System.currentTimeMillis() - 86400000L * 30,
                        mileage = 13000,
                        cost = 3000,
                        shop = "カーショップB",
                        memo = null
                    ),
                    Maintenance(
                        id = 3L,
                        carId = 1L,
                        type = MaintenanceType.VEHICLE_INSPECTION,
                        date = System.currentTimeMillis() - 86400000L * 180,
                        mileage = 10000,
                        cost = 80000,
                        shop = "ディーラー",
                        memo = "車検実施"
                    )
                ),
                isLoading = false,
                errorMessage = null
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onNavigateToCar = {},
            onNavigateToMaintenanceList = {}
        )
    }
}
