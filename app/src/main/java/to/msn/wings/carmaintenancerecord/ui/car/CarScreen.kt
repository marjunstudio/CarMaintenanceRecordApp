package to.msn.wings.carmaintenancerecord.ui.car

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import to.msn.wings.carmaintenancerecord.ui.theme.CarMaintenanceRecordTheme

@Composable
fun CarScreen(
    onNavigateToMaintenanceList: (Long) -> Unit = {},
    onNavigateBack: () -> Unit = {},
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
        onNavigateBack = onNavigateBack,
        onCarNameChanged = viewModel::onCarNameChanged,
        onCarManufacturerChanged = viewModel::onCarManufacturerChanged,
        onCarModelChanged = viewModel::onCarModelChanged,
        onCarYearChanged = viewModel::onCarYearChanged,
        onCarLicensePlateChanged = viewModel::onCarLicensePlateChanged,
        onCarInitialMileageChanged = viewModel::onCarInitialMileageChanged,
        onCarMileageChanged = viewModel::onCarMileageChanged,
        onSaveClick = { viewModel.saveCar() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarScreenContent(
    uiState: CarUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    onCarNameChanged: (String) -> Unit,
    onCarManufacturerChanged: (String) -> Unit,
    onCarModelChanged: (String) -> Unit,
    onCarYearChanged: (String) -> Unit,
    onCarLicensePlateChanged: (String) -> Unit,
    onCarInitialMileageChanged: (String) -> Unit,
    onCarMileageChanged: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "車両の追加・編集",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = onSaveClick,
                        enabled = !uiState.isSaving
                    ) {
                        Text(
                            text = "保存",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                    .padding(16.dp)
            ) {
                androidx.compose.material3.Button(
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isSaving,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = "車両を保存",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                PhotoUploadPlaceholder()

                Spacer(modifier = Modifier.height(16.dp))

                SectionHeader(title = "必須項目")

                FormField(
                    label = "愛車の名前",
                    value = uiState.carName,
                    onValueChange = onCarNameChanged,
                    placeholder = "例：マイシビック",
                    isRequired = true,
                    errorMessage = uiState.carNameError,
                    enabled = !uiState.isSaving
                )

                FormField(
                    label = "メーカー",
                    value = uiState.carManufacturer,
                    onValueChange = onCarManufacturerChanged,
                    placeholder = "例：ホンダ",
                    isRequired = true,
                    errorMessage = uiState.carManufacturerError,
                    enabled = !uiState.isSaving
                )

                FormField(
                    label = "車種",
                    value = uiState.carModel,
                    onValueChange = onCarModelChanged,
                    placeholder = "例：シビック",
                    isRequired = true,
                    errorMessage = uiState.carModelError,
                    enabled = !uiState.isSaving
                )

                FormField(
                    label = "年式",
                    value = uiState.carYear,
                    onValueChange = onCarYearChanged,
                    placeholder = "例：2021",
                    keyboardType = KeyboardType.Number,
                    isRequired = true,
                    errorMessage = uiState.carYearError,
                    enabled = !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(8.dp))

                SectionHeader(title = "任意項目")

                FormField(
                    label = "現在の走行距離",
                    value = uiState.carMileage,
                    onValueChange = onCarMileageChanged,
                    placeholder = "例：50000",
                    keyboardType = KeyboardType.Number,
                    errorMessage = uiState.carMileageError,
                    enabled = !uiState.isSaving
                )

                FormField(
                    label = "納車時走行距離",
                    value = uiState.carInitialMileage,
                    onValueChange = onCarInitialMileageChanged,
                    placeholder = "例：0",
                    keyboardType = KeyboardType.Number,
                    enabled = !uiState.isSaving
                )

                FormField(
                    label = "ナンバープレート",
                    value = uiState.carLicensePlate,
                    onValueChange = onCarLicensePlateChanged,
                    placeholder = "例：品川 300 あ 12-34",
                    enabled = !uiState.isSaving
                )

                Spacer(modifier = Modifier.height(112.dp))
            }
        }
    }
}

@Composable
private fun PhotoUploadPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(192.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
                Text(
                    text = "タップして写真を追加",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isRequired: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = if (isRequired) "$label *" else label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val isError = errorMessage != null

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder) },
            singleLine = true,
            isError = isError,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error
            ),
            shape = RoundedCornerShape(8.dp)
        )

        if (isError) {
            Text(
                text = errorMessage!!,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview(name = "新規登録画面", showBackground = true)
@Composable
private fun CarScreenPreview_New() {
    CarMaintenanceRecordTheme {
        CarScreenContent(
            uiState = CarUiState(
                car = null,
                isLoading = false,
                isSaving = false,
                errorMessage = null,
                carName = "",
                carManufacturer = "",
                carModel = "",
                carYear = "",
                carLicensePlate = "",
                carInitialMileage = "",
                carMileage = ""
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onNavigateBack = {},
            onCarNameChanged = {},
            onCarManufacturerChanged = {},
            onCarModelChanged = {},
            onCarYearChanged = {},
            onCarLicensePlateChanged = {},
            onCarInitialMileageChanged = {},
            onCarMileageChanged = {},
            onSaveClick = {}
        )
    }
}

@Preview(name = "エラー表示", showBackground = true)
@Composable
private fun CarScreenPreview_WithErrors() {
    CarMaintenanceRecordTheme {
        CarScreenContent(
            uiState = CarUiState(
                car = null,
                isLoading = false,
                isSaving = false,
                errorMessage = null,
                carName = "",
                carManufacturer = "",
                carModel = "",
                carYear = "",
                carLicensePlate = "",
                carInitialMileage = "",
                carMileage = "abc",
                carNameError = "愛車の名前を入力してください",
                carManufacturerError = "メーカーを選択してください",
                carModelError = "車種を入力してください",
                carYearError = "年式を入力してください",
                carMileageError = "走行距離は数字で入力してください"
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onNavigateBack = {},
            onCarNameChanged = {},
            onCarManufacturerChanged = {},
            onCarModelChanged = {},
            onCarYearChanged = {},
            onCarLicensePlateChanged = {},
            onCarInitialMileageChanged = {},
            onCarMileageChanged = {},
            onSaveClick = {}
        )
    }
}
