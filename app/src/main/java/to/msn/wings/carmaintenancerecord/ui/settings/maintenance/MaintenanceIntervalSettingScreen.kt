package to.msn.wings.carmaintenancerecord.ui.settings.maintenance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import to.msn.wings.carmaintenancerecord.ui.components.AppTopBar
import to.msn.wings.carmaintenancerecord.ui.theme.CarMaintenanceRecordTheme

@Composable
fun MaintenanceIntervalSettingScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: MaintenanceIntervalSettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    var selectedItem by remember { mutableStateOf<SettingItem?>(null) }

    MaintenanceIntervalSettingScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNavigateBack = onNavigateBack,
        onItemClick = { selectedItem = it }
    )

    selectedItem?.let { item ->
        IntervalSettingDialog(
            item = item,
            onDismiss = { selectedItem = null },
            onSave = { intervalKm, intervalDays ->
                viewModel.saveSetting(item.type, intervalKm, intervalDays)
                selectedItem = null
            },
            onReset = {
                viewModel.resetSetting(item.type)
                selectedItem = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaintenanceIntervalSettingScreenContent(
    uiState: MaintenanceIntervalSettingUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    onItemClick: (SettingItem) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                title = "メンテナンス履歴",
                onNavigateBack = onNavigateBack
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "各メンテナンス項目の実施周期をカスタマイズできます",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(uiState.settingItems) { item ->
                    SettingItemCard(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun SettingItemCard(
    item: SettingItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
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
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = if (item.isCustomized) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.isCustomized) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = item.type.icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column {
                    Text(
                        text = item.type.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (item.isCustomized) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(
                        text = item.currentValueText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MaintenanceIntervalSettingScreenPreview() {
    CarMaintenanceRecordTheme {
        MaintenanceIntervalSettingScreenContent(
            uiState = MaintenanceIntervalSettingUiState(
                carId = 1L,
                settingItems = listOf(
                    SettingItem(
                        type = MaintenanceType.OIL_CHANGE,
                        customIntervalKm = 6000,
                        customIntervalDays = null,
                        defaultIntervalKm = 5000,
                        defaultIntervalDays = null
                    ),
                    SettingItem(
                        type = MaintenanceType.TIRE_ROTATION,
                        customIntervalKm = null,
                        customIntervalDays = null,
                        defaultIntervalKm = 10000,
                        defaultIntervalDays = null
                    ),
                ),
                isLoading = false,
                errorMessage = null
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onNavigateBack = {},
            onItemClick = {}
        )
    }
}
