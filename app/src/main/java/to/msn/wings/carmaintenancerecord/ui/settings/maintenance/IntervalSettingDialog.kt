package to.msn.wings.carmaintenancerecord.ui.settings.maintenance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import to.msn.wings.carmaintenancerecord.domain.model.IntervalUnit
import to.msn.wings.carmaintenancerecord.domain.model.MaintenanceType
import to.msn.wings.carmaintenancerecord.ui.theme.CarMaintenanceRecordTheme

@Composable
fun IntervalSettingDialog(
    item: SettingItem,
    onDismiss: () -> Unit,
    onSave: (intervalKm: Int?, intervalDays: Int?) -> Unit,
    onReset: () -> Unit
) {
    val supportsDistance = item.type.intervalUnit == IntervalUnit.DISTANCE_ONLY ||
            item.type.intervalUnit == IntervalUnit.BOTH
    val supportsDays = item.type.intervalUnit == IntervalUnit.DAYS_ONLY ||
            item.type.intervalUnit == IntervalUnit.BOTH

    var enableDistance by remember {
        mutableStateOf(
            when (item.type.intervalUnit) {
                IntervalUnit.DISTANCE_ONLY -> true
                IntervalUnit.BOTH -> item.customIntervalKm != null || item.defaultIntervalKm != null
                else -> false
            }
        )
    }
    var enableDays by remember {
        mutableStateOf(
            when (item.type.intervalUnit) {
                IntervalUnit.DAYS_ONLY -> true
                IntervalUnit.BOTH -> item.customIntervalDays != null || item.defaultIntervalDays != null
                else -> false
            }
        )
    }

    var distanceText by remember {
        mutableStateOf(
            (item.customIntervalKm ?: item.defaultIntervalKm)?.toString() ?: ""
        )
    }
    var daysText by remember {
        mutableStateOf(
            (item.customIntervalDays ?: item.defaultIntervalDays)?.toString() ?: ""
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Text(
                    text = item.type.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "実施周期を設定してください",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (supportsDistance) {
                    if (item.type.intervalUnit == IntervalUnit.BOTH) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = enableDistance,
                                onCheckedChange = { enableDistance = it }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "距離で管理",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    if (enableDistance) {
                        OutlinedTextField(
                            value = distanceText,
                            onValueChange = { distanceText = it.filter { char -> char.isDigit() } },
                            label = { Text("走行距離（km）") },
                            placeholder = {
                                Text(
                                    text = item.defaultIntervalKm?.let { "デフォルト: ${it}km" } ?: ""
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = if (item.type.intervalUnit == IntervalUnit.BOTH) 48.dp else 0.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (supportsDays) {
                    if (item.type.intervalUnit == IntervalUnit.BOTH) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = enableDays,
                                onCheckedChange = { enableDays = it }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "日数で管理",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    if (enableDays) {
                        OutlinedTextField(
                            value = daysText,
                            onValueChange = { daysText = it.filter { char -> char.isDigit() } },
                            label = { Text("日数") },
                            placeholder = {
                                Text(
                                    text = item.defaultIntervalDays?.let { "デフォルト: ${it}日" } ?: ""
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = if (item.type.intervalUnit == IntervalUnit.BOTH) 48.dp else 0.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (item.isCustomized) {
                        TextButton(
                            onClick = onReset,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("デフォルトに戻す")
                        }
                    }

                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("キャンセル")
                    }

                    TextButton(
                        onClick = {
                            val km = if (enableDistance && distanceText.isNotBlank()) {
                                distanceText.toIntOrNull()
                            } else {
                                null
                            }
                            val days = if (enableDays && daysText.isNotBlank()) {
                                daysText.toIntOrNull()
                            } else {
                                null
                            }
                            onSave(km, days)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("保存")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun IntervalSettingDialogPreview() {
    CarMaintenanceRecordTheme {
        IntervalSettingDialog(
            item = SettingItem(
                type = MaintenanceType.OIL_CHANGE,
                customIntervalKm = null,
                customIntervalDays = null,
                defaultIntervalKm = 5000,
                defaultIntervalDays = null
            ),
            onDismiss = {},
            onSave = { _, _ -> },
            onReset = {}
        )
    }
}
