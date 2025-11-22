package to.msn.wings.carmaintenancerecord.ui.maintenance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import to.msn.wings.carmaintenancerecord.ui.theme.CarMaintenanceRecordTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceDetailScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: MaintenanceDetailViewModel = hiltViewModel()
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

    // 保存完了時の処理
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.maintenance == null) "新規メンテナンス記録" else "メンテナンス記録編集"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                MaintenanceForm(
                    uiState = uiState,
                    onTypeChange = viewModel::updateType,
                    onDateChange = viewModel::updateDate,
                    onMileageChange = viewModel::updateMileage,
                    onCostChange = viewModel::updateCost,
                    onShopChange = viewModel::updateShop,
                    onMemoChange = viewModel::updateMemo,
                    onSave = viewModel::saveMaintenance,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

/**
 * メンテナンス入力フォーム
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaintenanceForm(
    uiState: MaintenanceDetailUiState,
    onTypeChange: (MaintenanceType) -> Unit,
    onDateChange: (Long) -> Unit,
    onMileageChange: (String) -> Unit,
    onCostChange: (String) -> Unit,
    onShopChange: (String) -> Unit,
    onMemoChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // メンテナンス種別
        MaintenanceTypeDropdown(
            selectedType = uiState.selectedType,
            onTypeChange = onTypeChange
        )

        // 実施日
        DateSelector(
            dateMillis = uiState.dateMillis,
            onDateChange = onDateChange
        )

        // 走行距離
        OutlinedTextField(
            value = uiState.mileageText,
            onValueChange = onMileageChange,
            label = { Text("走行距離 (km) *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 費用
        OutlinedTextField(
            value = uiState.costText,
            onValueChange = onCostChange,
            label = { Text("費用 (円)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // 実施店舗
        OutlinedTextField(
            value = uiState.shopText,
            onValueChange = onShopChange,
            label = { Text("実施店舗") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // メモ
        OutlinedTextField(
            value = uiState.memoText,
            onValueChange = onMemoChange,
            label = { Text("メモ") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        // 保存ボタン
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text("保存")
        }

        // 必須項目の注釈
        Text(
            text = "* は必須項目です",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * メンテナンス種別ドロップダウン
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaintenanceTypeDropdown(
    selectedType: MaintenanceType,
    onTypeChange: (MaintenanceType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedType.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("メンテナンス種別 *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            MaintenanceType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) },
                    onClick = {
                        onTypeChange(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * 日付選択
 */
@Composable
private fun DateSelector(
    dateMillis: Long,
    onDateChange: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("yyyy年MM月dd日", Locale.JAPANESE) }

    OutlinedTextField(
        value = dateFormatter.format(dateMillis),
        onValueChange = {},
        label = { Text("実施日 *") },
        readOnly = true,
        modifier = modifier.fillMaxWidth(),
        trailingIcon = {
            TextButton(onClick = { showDatePicker = true }) {
                Text("変更")
            }
        }
    )

    if (showDatePicker) {
        SimpleDatePickerDialog(
            initialDateMillis = dateMillis,
            onDateSelected = { selectedMillis ->
                onDateChange(selectedMillis)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

/**
 * シンプルな日付選択ダイアログ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleDatePickerDialog(
    initialDateMillis: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = remember { Calendar.getInstance() }
    calendar.timeInMillis = initialDateMillis

    androidx.compose.material3.DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(calendar.timeInMillis)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    ) {
        androidx.compose.material3.DatePicker(
            state = androidx.compose.material3.rememberDatePickerState(
                initialSelectedDateMillis = initialDateMillis
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}

// ========== Previews ==========

@Preview(showBackground = true)
@Composable
private fun MaintenanceFormPreview() {
    CarMaintenanceRecordTheme {
        MaintenanceForm(
            uiState = MaintenanceDetailUiState(
                selectedType = MaintenanceType.OIL_CHANGE,
                dateMillis = System.currentTimeMillis(),
                mileageText = "50000",
                costText = "5000",
                shopText = "オートバックス",
                memoText = "定期メンテナンス"
            ),
            onTypeChange = {},
            onDateChange = {},
            onMileageChange = {},
            onCostChange = {},
            onShopChange = {},
            onMemoChange = {},
            onSave = {}
        )
    }
}
