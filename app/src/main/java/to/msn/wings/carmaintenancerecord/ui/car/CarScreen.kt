package to.msn.wings.carmaintenancerecord.ui.car

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import to.msn.wings.carmaintenancerecord.ui.theme.CarMaintenanceRecordTheme
import to.msn.wings.carmaintenancerecord.util.formatTimestamp

@Composable
fun CarScreen(
    viewModel: CarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    CarScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onCarNameChanged = viewModel::onCarNameChanged,
        onCarManufacturerChanged = viewModel::onCarManufacturerChanged,
        onCarModelChanged = viewModel::onCarModelChanged,
        onCarYearChanged = viewModel::onCarYearChanged,
        onCarLicensePlateChanged = viewModel::onCarLicensePlateChanged,
        onCarInitialMileageChanged = viewModel::onCarInitialMileageChanged,
        onCarMileageChanged = viewModel::onCarMileageChanged,
        onSaveOrUpdateClick = {
            if (uiState.car == null) {
                viewModel.saveCar()
            } else {
                viewModel.updateMileage()
            }
        }
    )
}

@Composable
private fun CarScreenContent(
    uiState: CarUiState,
    snackbarHostState: SnackbarHostState,
    onCarNameChanged: (String) -> Unit,
    onCarManufacturerChanged: (String) -> Unit,
    onCarModelChanged: (String) -> Unit,
    onCarYearChanged: (String) -> Unit,
    onCarLicensePlateChanged: (String) -> Unit,
    onCarInitialMileageChanged: (String) -> Unit,
    onCarMileageChanged: (String) -> Unit,
    onSaveOrUpdateClick: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = if (uiState.car == null) "車両登録" else "車両情報",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                OutlinedTextField(
                    value = uiState.carName,
                    onValueChange = onCarNameChanged,
                    label = { Text("車名（愛称）") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = uiState.car == null && !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.carManufacturer,
                    onValueChange = onCarManufacturerChanged,
                    label = { Text("メーカー") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = uiState.car == null && !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.carModel,
                    onValueChange = onCarModelChanged,
                    label = { Text("車種/モデル") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = uiState.car == null && !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.carYear,
                    onValueChange = onCarYearChanged,
                    label = { Text("年式") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = uiState.car == null && !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.carLicensePlate,
                    onValueChange = onCarLicensePlateChanged,
                    label = { Text("ナンバープレート（任意）") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = uiState.car == null && !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.carInitialMileage,
                    onValueChange = onCarInitialMileageChanged,
                    label = { Text("納車時走行距離 (km)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = uiState.car == null && !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.carMileage,
                    onValueChange = onCarMileageChanged,
                    label = { Text("現在の走行距離 (km)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onSaveOrUpdateClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isSaving
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text(if (uiState.car == null) "登録" else "走行距離を更新")
                    }
                }

                if (uiState.car != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "登録日: ${formatTimestamp(uiState.car!!.createdAt)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "更新日: ${formatTimestamp(uiState.car!!.updatedAt)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Preview(name = "新規登録（空のフォーム）", showBackground = true)
@Composable
private fun CarScreenPreview_Empty() {
    CarMaintenanceRecordTheme {
        CarScreenContent(
            uiState = CarUiState(
                car = null,
                isLoading = false,
                errorMessage = null,
                carName = "",
                carManufacturer = "",
                carModel = "",
                carYear = "",
                carLicensePlate = "",
                carInitialMileage = "",
                carMileage = "",
                isSaving = false
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onCarNameChanged = {},
            onCarManufacturerChanged = {},
            onCarModelChanged = {},
            onCarYearChanged = {},
            onCarLicensePlateChanged = {},
            onCarInitialMileageChanged = {},
            onCarMileageChanged = {},
            onSaveOrUpdateClick = {}
        )
    }
}
