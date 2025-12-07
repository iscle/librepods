/*
    LibrePods - AirPods liberated from Apple’s ecosystem
    Copyright (C) 2025 LibrePods contributors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package me.kavishdevar.librepods.presentation.ui.settings

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kyant.backdrop.backdrops.layerBackdrop
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.ui.component.LiquidIconButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidScaffold
import me.kavishdevar.librepods.presentation.ui.component.LiquidTopAppBar
import me.kavishdevar.librepods.presentation.ui.component.NavigationButton
import me.kavishdevar.librepods.presentation.ui.component.SectionTitle
import me.kavishdevar.librepods.presentation.ui.component.StyledSlider
import me.kavishdevar.librepods.presentation.ui.component.StyledToggle
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme
import me.kavishdevar.librepods.util.RadareOffsetFinder

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTroubleshooting: () -> Unit,
    onNavigateToOpenSourceLicenses: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onShowCameraPackageDialog: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    SettingsContent(
        onNavigateBack = onNavigateBack,
        onNavigateToTroubleshooting = onNavigateToTroubleshooting,
        onNavigateToOpenSourceLicenses = onNavigateToOpenSourceLicenses,
        onNavigateToOnboarding = onNavigateToOnboarding,
        onShowCameraPackageDialog = onShowCameraPackageDialog,
    )
}

@Composable
fun SettingsContent(
    onNavigateBack: () -> Unit,
    onNavigateToTroubleshooting: () -> Unit,
    onNavigateToOpenSourceLicenses: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    onShowCameraPackageDialog: () -> Unit,
) {
    isSystemInDarkTheme()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showResetDialog by remember { mutableStateOf(false) }
    var showIrkDialog by remember { mutableStateOf(false) }
    var showEncKeyDialog by remember { mutableStateOf(false) }
    var showCameraDialog by remember { mutableStateOf(false) }
    var irkValue by remember { mutableStateOf("") }
    var encKeyValue by remember { mutableStateOf("") }
    var cameraPackageValue by remember { mutableStateOf("") }
    var irkError by remember { mutableStateOf<String?>(null) }
    var encKeyError by remember { mutableStateOf<String?>(null) }
    var cameraPackageError by remember { mutableStateOf<String?>(null) }

    // todo: move to viewmodel
    fun validateHexInput(input: String): Boolean {
        val hexPattern = Regex("^[0-9a-fA-F]{32}$")
        return hexPattern.matches(input)
    }

    var isProcessingSdp by remember { mutableStateOf(false) }
    var actAsAppleDevice by remember { mutableStateOf(false) }

    BackHandler(enabled = isProcessingSdp) {}

    LiquidScaffold(
        topBar = {
            LiquidTopAppBar(
                title = {
                    Text(stringResource(R.string.settings))
                },
                navigationIcon = {
                    LiquidIconButton(
                        onClick = onNavigateBack,
                        backdrop = backdrop,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Text("\uDBC2\uDFF6")
                    }
                },
                backdrop = backdrop
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            val isDarkTheme = isSystemInDarkTheme()
            val backgroundColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFFFFFFF)
            val textColor = if (isDarkTheme) Color.White else Color.Black

            StyledToggle(
                title = stringResource(R.string.widget),
                label = stringResource(R.string.show_phone_battery_in_widget),
                description = stringResource(R.string.show_phone_battery_in_widget_description),
                isChecked = true,
                onCheckedChange = {}
            )

            SectionTitle(
                title = stringResource(R.string.conversational_awareness),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        backgroundColor,
                        RoundedCornerShape(28.dp)
                    )
                    .padding(vertical = 4.dp)
            ) {
                StyledToggle(
                    label = stringResource(R.string.conversational_awareness_pause_music),
                    description = stringResource(R.string.conversational_awareness_pause_music_description),
                    isChecked = true,
                    onCheckedChange = {},
                    independent = false
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0x40888888),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )

                StyledToggle(
                    label = stringResource(R.string.relative_conversational_awareness_volume),
                    description = stringResource(R.string.relative_conversational_awareness_volume_description),
                    isChecked = true,
                    onCheckedChange = {},
                    independent = false
                )
            }

            Spacer(Modifier.height(16.dp))

            var conversationalAwarenessVolume by remember { mutableFloatStateOf(43f) }

            StyledSlider(
                label = stringResource(R.string.conversational_awareness_volume),
                value = conversationalAwarenessVolume,
                valueRange = 10f..85f,
                startLabel = "10%",
                endLabel = "85%",
                onValueChange = { newValue -> conversationalAwarenessVolume = newValue },
                independent = true
            )

            Spacer(Modifier.height(16.dp))

            NavigationButton(
                name = stringResource(R.string.set_custom_camera_package),
                title = stringResource(R.string.camera_control),
                description = stringResource(R.string.camera_control_app_description),
                onClick = onShowCameraPackageDialog,
                independent = true,
            )

            Spacer(Modifier.height(16.dp))

            StyledToggle(
                title = stringResource(R.string.quick_settings_tile),
                label = stringResource(R.string.open_dialog_for_controlling),
                description = stringResource(R.string.open_dialog_for_controlling_description),
                isChecked = true,
                onCheckedChange = {}
            )

            Spacer(Modifier.height(16.dp))

            StyledToggle(
                title = stringResource(R.string.ear_detection),
                label = stringResource(R.string.disconnect_when_not_wearing),
                description = stringResource(R.string.disconnect_when_not_wearing_description),
                isChecked = true,
                onCheckedChange = {}
            )

            SectionTitle(
                title = stringResource(R.string.takeover_airpods_state),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        backgroundColor,
                        RoundedCornerShape(28.dp)
                    )
                    .padding(vertical = 4.dp)
            ) {
                StyledToggle(
                    label = stringResource(R.string.takeover_disconnected),
                    description = stringResource(R.string.takeover_disconnected_desc),
                    isChecked = true,
                    onCheckedChange = {},
                    independent = false
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0x40888888),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )

                StyledToggle(
                    label = stringResource(R.string.takeover_idle),
                    description = stringResource(R.string.takeover_idle_desc),
                    isChecked = true,
                    onCheckedChange = {},
                    independent = false
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0x40888888),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )

                StyledToggle(
                    label = stringResource(R.string.takeover_music),
                    description = stringResource(R.string.takeover_music_desc),
                    isChecked = true,
                    onCheckedChange = {},
                    independent = false
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0x40888888),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )

                StyledToggle(
                    label = stringResource(R.string.takeover_call),
                    description = stringResource(R.string.takeover_call_desc),
                    isChecked = true,
                    onCheckedChange = {},
                    independent = false
                )
            }

            Spacer(Modifier.height(16.dp))

            SectionTitle(
                title = stringResource(R.string.takeover_phone_state),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        backgroundColor,
                        RoundedCornerShape(28.dp)
                    )
                    .padding(vertical = 4.dp)
            ) {
                StyledToggle(
                    label = stringResource(R.string.takeover_ringing_call),
                    description = stringResource(R.string.takeover_ringing_call_desc),
                    isChecked = true,
                    onCheckedChange = {},
                    independent = false
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0x40888888),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )

                StyledToggle(
                    label = stringResource(R.string.takeover_media_start),
                    description = stringResource(R.string.takeover_media_start_desc),
                    isChecked = true,
                    onCheckedChange = {},
                    independent = false
                )
            }

            SectionTitle(
                title = stringResource(R.string.advanced_options),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        backgroundColor,
                        RoundedCornerShape(28.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = { showIrkDialog = true },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                            .padding(end = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.set_identity_resolving_key),
                            fontSize = 16.sp,
                            color = textColor
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.set_identity_resolving_key_description),
                            fontSize = 14.sp,
                            color = textColor.copy(0.6f),
                            lineHeight = 16.sp,
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0x40888888),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = { showEncKeyDialog = true },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp)
                            .padding(end = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.set_encryption_key),
                            fontSize = 16.sp,
                            color = textColor
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.set_encryption_key_description),
                            fontSize = 14.sp,
                            color = textColor.copy(0.6f),
                            lineHeight = 16.sp,
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            StyledToggle(
                label = stringResource(R.string.use_alternate_head_tracking_packets),
                description = stringResource(R.string.use_alternate_head_tracking_packets_description),
                isChecked = true,
                onCheckedChange = {},
                independent = true
            )

            Spacer(Modifier.height(16.dp))

            NavigationButton(
                name = stringResource(R.string.troubleshooting),
                description = stringResource(R.string.troubleshooting_description),
                onClick = onNavigateToTroubleshooting,
                independent = true
            )

            LaunchedEffect(Unit) {
                actAsAppleDevice = RadareOffsetFinder.isSdpOffsetAvailable()
            }
            stringResource(R.string.found_offset_restart_bluetooth)

            StyledToggle(
                label = stringResource(R.string.act_as_an_apple_device),
                description = stringResource(R.string.act_as_an_apple_device_description),
                isChecked = true,
                onCheckedChange = {},
                independent = true,
                enabled = !isProcessingSdp
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { showResetDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.reset_hook_offset),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            NavigationButton(
                name = stringResource(R.string.open_source_licenses),
                onClick = onNavigateToOpenSourceLicenses,
                independent = true
            )

            Spacer(Modifier.height(32.dp))

            if (showResetDialog) {
                AlertDialog(
                    onDismissRequest = { showResetDialog = false },
                    title = {
                        Text(
                            "Reset Hook Offset",
                            fontWeight = FontWeight.Medium
                        )
                    },
                    text = {
                        Text(
                            stringResource(R.string.reset_hook_offset_description),
                        )
                    },
                    confirmButton = {
                        val successText = stringResource(R.string.hook_offset_reset_success)
                        val failureText = stringResource(R.string.hook_offset_reset_failure)
                        TextButton(
                            onClick = {
                                if (RadareOffsetFinder.clearHookOffsets()) {
                                    Toast.makeText(
                                        context,
                                        successText,
                                        Toast.LENGTH_LONG
                                    ).show()

                                    onNavigateToOnboarding()
                                } else {
                                    Toast.makeText(
                                        context,
                                        failureText,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                showResetDialog = false
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text(
                                stringResource(R.string.reset),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showResetDialog = false }
                        ) {
                            Text(
                                "Cancel",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                )
            }

            if (showIrkDialog) {
                AlertDialog(
                    onDismissRequest = { showIrkDialog = false },
                    title = {
                        Text(
                            stringResource(R.string.set_identity_resolving_key),
                            fontWeight = FontWeight.Medium
                        )
                    },
                    text = {
                        Column {
                            Text(
                                stringResource(R.string.enter_irk_hex),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = irkValue,
                                onValueChange = {
                                    irkValue = it.lowercase()
                                        .filter { char -> char.isDigit() || char in 'a'..'f' }
                                    irkError = null
                                },
                                modifier = Modifier.fillMaxWidth(),
                                isError = irkError != null,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Ascii,
                                    capitalization = KeyboardCapitalization.None
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = if (isDarkTheme) Color(0xFF007AFF) else Color(
                                        0xFF3C6DF5
                                    ),
                                    unfocusedBorderColor = if (isDarkTheme) Color.Gray else Color.LightGray
                                ),
                                supportingText = {
                                    if (irkError != null) {
                                        Text(
                                            stringResource(R.string.must_be_32_hex_chars),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                label = { Text(stringResource(R.string.irk_hex_value)) }
                            )
                        }
                    },
                    confirmButton = {
                        val successText = stringResource(R.string.irk_set_success)
                        val errorText = stringResource(R.string.error_converting_hex)
                        TextButton(
                            onClick = {
                                if (!validateHexInput(irkValue)) {
                                    irkError = "Must be exactly 32 hex characters"
                                    return@TextButton
                                }

                                try {
                                    Toast.makeText(context, successText, Toast.LENGTH_SHORT).show()
                                    showIrkDialog = false
                                } catch (e: Exception) {
                                    irkError = errorText + " " + (e.message ?: "Unknown error")
                                }
                            }
                        ) {
                            Text(
                                "Save",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showIrkDialog = false }
                        ) {
                            Text(
                                "Cancel",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                )
            }

            if (showEncKeyDialog) {
                AlertDialog(
                    onDismissRequest = { showEncKeyDialog = false },
                    title = {
                        Text(
                            stringResource(R.string.set_encryption_key),
                            fontWeight = FontWeight.Medium
                        )
                    },
                    text = {
                        Column {
                            Text(
                                stringResource(R.string.enter_enc_key_hex),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = encKeyValue,
                                onValueChange = {
                                    encKeyValue = it.lowercase()
                                        .filter { char -> char.isDigit() || char in 'a'..'f' }
                                    encKeyError = null
                                },
                                modifier = Modifier.fillMaxWidth(),
                                isError = encKeyError != null,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Ascii,
                                    capitalization = KeyboardCapitalization.None
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = if (isDarkTheme) Color(0xFF007AFF) else Color(
                                        0xFF3C6DF5
                                    ),
                                    unfocusedBorderColor = if (isDarkTheme) Color.Gray else Color.LightGray
                                ),
                                supportingText = {
                                    if (encKeyError != null) {
                                        Text(
                                            stringResource(R.string.must_be_32_hex_chars),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                label = { Text(stringResource(R.string.enc_key_hex_value)) }
                            )
                        }
                    },
                    confirmButton = {
                        val successText = stringResource(R.string.encryption_key_set_success)
                        val errorText = stringResource(R.string.error_converting_hex)
                        TextButton(
                            onClick = {
                                if (!validateHexInput(encKeyValue)) {
                                    encKeyError = "Must be exactly 32 hex characters"
                                    return@TextButton
                                }

                                try {
                                    Toast.makeText(context, successText, Toast.LENGTH_SHORT).show()
                                    showEncKeyDialog = false
                                } catch (e: Exception) {
                                    encKeyError = errorText + " " + (e.message ?: "Unknown error")
                                }
                            }
                        ) {
                            Text(
                                "Save",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showEncKeyDialog = false }
                        ) {
                            Text(
                                "Cancel",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                )
            }

            if (showCameraDialog) {
                AlertDialog(
                    onDismissRequest = { showCameraDialog = false },
                    title = {
                        Text(
                            stringResource(R.string.set_custom_camera_package),
                            fontWeight = FontWeight.Medium
                        )
                    },
                    text = {
                        Column {
                            Text(
                                stringResource(R.string.enter_custom_camera_package),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = cameraPackageValue,
                                onValueChange = {
                                    cameraPackageValue = it
                                    cameraPackageError = null
                                },
                                modifier = Modifier.fillMaxWidth(),
                                isError = cameraPackageError != null,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Ascii,
                                    capitalization = KeyboardCapitalization.None
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = if (isDarkTheme) Color(0xFF007AFF) else Color(
                                        0xFF3C6DF5
                                    ),
                                    unfocusedBorderColor = if (isDarkTheme) Color.Gray else Color.LightGray
                                ),
                                supportingText = {
                                    if (cameraPackageError != null) {
                                        Text(
                                            cameraPackageError!!,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                label = { Text(stringResource(R.string.custom_camera_package)) }
                            )
                        }
                    },
                    confirmButton = {
                        val successText = stringResource(R.string.custom_camera_package_set_success)
                        TextButton(
                            onClick = {
                                if (cameraPackageValue.isBlank()) {
                                    Toast.makeText(context, successText, Toast.LENGTH_SHORT).show()
                                    showCameraDialog = false
                                    return@TextButton
                                }

                                Toast.makeText(context, successText, Toast.LENGTH_SHORT).show()
                                showCameraDialog = false
                            }
                        ) {
                            Text(
                                "Save",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showCameraDialog = false }
                        ) {
                            Text(
                                "Cancel",
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun SettingsScreenLightPreview() {
    LibrePodsTheme {
        SettingsContent(
            onNavigateBack = {},
            onNavigateToTroubleshooting = {},
            onNavigateToOpenSourceLicenses = {},
            onNavigateToOnboarding = {},
            onShowCameraPackageDialog = {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SettingsScreenDarkPreview() {
    LibrePodsTheme {
        SettingsContent(
            onNavigateBack = {},
            onNavigateToTroubleshooting = {},
            onNavigateToOpenSourceLicenses = {},
            onNavigateToOnboarding = {},
            onShowCameraPackageDialog = {}
        )
    }
}
