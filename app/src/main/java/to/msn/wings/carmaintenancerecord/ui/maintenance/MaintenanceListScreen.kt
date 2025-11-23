package to.msn.wings.carmaintenancerecord.ui.maintenance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import to.msn.wings.carmaintenancerecord.domain.model.Maintenance
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import to.msn.wings.carmaintenancerecord.ui.theme.CarMaintenanceRecordTheme
import to.msn.wings.carmaintenancerecord.util.formatTimestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceListScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToDetail: (Long) -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    viewModel: MaintenanceListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // エラーメッセージの表示
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("メンテナンス履歴") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "メンテナンス記録を追加"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.maintenanceList.isEmpty() -> {
                    EmptyState(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    MaintenanceList(
                        maintenanceList = uiState.maintenanceList,
                        onMaintenanceClick = onNavigateToDetail,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * メンテナンス履歴リスト（タイムライン形式）
 */
@Composable
private fun MaintenanceList(
    maintenanceList: List<Maintenance>,
    onMaintenanceClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        items(
            items = maintenanceList,
            key = { it.id }
        ) { maintenance ->
            val isFirst = maintenanceList.first() == maintenance
            val isLast = maintenanceList.last() == maintenance

            TimelineItem(
                maintenance = maintenance,
                isFirst = isFirst,
                isLast = isLast,
                onClick = { onMaintenanceClick(maintenance.id) }
            )
        }
    }
}

/**
 * タイムラインアイテム
 */
@Composable
private fun TimelineItem(
    maintenance: Maintenance,
    isFirst: Boolean,
    isLast: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        // 左側: アイコンと縦線
        TimelineIconColumn(
            icon = maintenance.type.icon,
            isFirst = isFirst,
            isLast = isLast,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Spacer(modifier = Modifier.padding(horizontal = 8.dp))

        // 右側: カードコンテンツ
        TimelineCard(
            maintenance = maintenance,
            onClick = onClick,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        )
    }
}

/**
 * タイムラインアイコン列
 */
@Composable
private fun TimelineIconColumn(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 上部の線
        if (!isFirst) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }

        // アイコン
        Surface(
            shape = androidx.compose.foundation.shape.CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        ) {
            Box(
                modifier = Modifier.padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        // 下部の線
        if (!isLast) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
    }
}

/**
 * タイムラインカード
 */
@Composable
private fun TimelineCard(
    maintenance: Maintenance,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // メンテナンス種別
            Text(
                text = maintenance.type.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 日付と走行距離
            Text(
                text = "${formatTimestamp(maintenance.date, "yyyy年MM月dd日")} · ${
                    String.format(
                        "%,d",
                        maintenance.mileage
                    )
                } km",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 費用
            if (maintenance.hasCost()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = maintenance.getCostDisplay(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * 空状態の表示
 */
@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "メンテナンス履歴がありません",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "右下のボタンから記録を追加できます",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// ========== Previews ==========

@Preview(showBackground = true)
@Composable
private fun TimelineItemPreview() {
    CarMaintenanceRecordTheme {
        Column {
            TimelineItem(
                maintenance = Maintenance(
                    id = 1L,
                    carId = 1L,
                    type = MaintenanceType.OIL_CHANGE,
                    date = System.currentTimeMillis(),
                    mileage = 55000,
                    cost = 8500,
                    shop = null,
                    memo = null
                ),
                isFirst = true,
                isLast = false,
                onClick = {}
            )
            TimelineItem(
                maintenance = Maintenance(
                    id = 2L,
                    carId = 1L,
                    type = MaintenanceType.LEGAL_INSPECTION,
                    date = System.currentTimeMillis() - 15552000000L,
                    mileage = 48000,
                    cost = 25000,
                    shop = null,
                    memo = null
                ),
                isFirst = false,
                isLast = true,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyStatePreview() {
    CarMaintenanceRecordTheme {
        EmptyState()
    }
}
