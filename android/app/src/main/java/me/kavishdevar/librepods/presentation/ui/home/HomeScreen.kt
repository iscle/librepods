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

package me.kavishdevar.librepods.presentation.ui.home

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.domain.model.AirPodsModel
import me.kavishdevar.librepods.presentation.ui.component.AboutCard
import me.kavishdevar.librepods.presentation.ui.component.AudioSettings
import me.kavishdevar.librepods.presentation.ui.component.BatteryView
import me.kavishdevar.librepods.presentation.ui.component.CallControlSettings
import me.kavishdevar.librepods.presentation.ui.component.ConfirmationDialog
import me.kavishdevar.librepods.presentation.ui.component.ConnectionSettings
import me.kavishdevar.librepods.presentation.ui.component.HearingHealthSettings
import me.kavishdevar.librepods.presentation.ui.component.LiquidButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidIconButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidScaffold
import me.kavishdevar.librepods.presentation.ui.component.LiquidTopAppBar
import me.kavishdevar.librepods.presentation.ui.component.MicrophoneSettings
import me.kavishdevar.librepods.presentation.ui.component.NavigationButton
import me.kavishdevar.librepods.presentation.ui.component.NoiseControlSettings
import me.kavishdevar.librepods.presentation.ui.component.PressAndHoldSettings
import me.kavishdevar.librepods.presentation.ui.component.StyledToggle
import me.kavishdevar.librepods.presentation.ui.rename.RenameDialog
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme

@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToHearingProtection: () -> Unit,
    onNavigateToHearingAid: () -> Unit,
    onNavigateToLeft: () -> Unit,
    onNavigateToRight: () -> Unit,
    onNavigateToCameraRemote: () -> Unit,
    onNavigateToAdaptiveAudio: () -> Unit,
    onNavigateToHeadGestures: () -> Unit,
    onNavigateToAccessibility: () -> Unit,
    onNavigateToVersion: () -> Unit,
    onNavigateToDebug: () -> Unit,
    onNavigateToTroubleshooting: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToHearingProtection = onNavigateToHearingProtection,
        onNavigateToHearingAid = onNavigateToHearingAid,
        onNavigateToLeft = onNavigateToLeft,
        onNavigateToRight = onNavigateToRight,
        onNavigateToCameraRemote = onNavigateToCameraRemote,
        onNavigateToAdaptiveAudio = onNavigateToAdaptiveAudio,
        onNavigateToHeadGestures = onNavigateToHeadGestures,
        onNavigateToAccessibility = onNavigateToAccessibility,
        onNavigateToVersion = onNavigateToVersion,
        onNavigateToDebug = onNavigateToDebug,
        onNavigateToTroubleshooting = onNavigateToTroubleshooting,
        onDonationDialogShown = {
            homeViewModel.setDonationDialogShown()
        },
        onReconnectToLastDevice = {
            homeViewModel.reconnectToLastDevice()
        },
    )
}

@Composable
fun HomeContent(
    state: HomeUiState,
    onNavigateToSettings: () -> Unit,
    onNavigateToHearingProtection: () -> Unit,
    onNavigateToHearingAid: () -> Unit,
    onNavigateToLeft: () -> Unit,
    onNavigateToRight: () -> Unit,
    onNavigateToCameraRemote: () -> Unit,
    onNavigateToAdaptiveAudio: () -> Unit,
    onNavigateToHeadGestures: () -> Unit,
    onNavigateToAccessibility: () -> Unit,
    onNavigateToVersion: () -> Unit,
    onNavigateToDebug: () -> Unit,
    onNavigateToTroubleshooting: () -> Unit,
    onDonationDialogShown: () -> Unit,
    onReconnectToLastDevice: () -> Unit,
) {
    val deviceName = state.name
    val isConnected = state.isConnected

    LiquidScaffold(
        topBar = {
            LiquidTopAppBar(
                title = {
                    Text(deviceName)
                },
                actions = {
                    LiquidIconButton(
                        onClick = onNavigateToSettings,
                        backdrop = backdrop
                    ) {
                        Text("\uDBC0\uDF5F") // Settings icon
                    }
                },
                backdrop = backdrop
            )
        }
    ) { innerPadding ->
        if (isConnected) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .layerBackdrop(backdrop)
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BatteryView(
                    model = AirPodsModel.AIRPODS_PRO_1
                )

                var showRenameDialog by remember { mutableStateOf(false) }

                NavigationButton(
                    onClick = { showRenameDialog = true }, //onNavigateToRename,
                    name = stringResource(R.string.name),
                    currentState = deviceName,
                    independent = true
                )

                if (showRenameDialog) {
                    RenameDialog(
                        name = deviceName,
                        onNameChange = {},
                        onDismissRequest = { showRenameDialog = false }
                    )
                }

                if (state.hasHearingHealthCapability) {
                    HearingHealthSettings(
                        hasPpeCapability = state.hasPpeCapability,
                        onNavigateToHearingProtection = onNavigateToHearingProtection,
                        onNavigateToHearingAid = onNavigateToHearingAid
                    )
                }

                if (state.hasListeningModeCapability) {
                    NoiseControlSettings()
                }

                if (state.hasStemConfigCapability) {
                    PressAndHoldSettings(
                        onNavigateToLeftLongPress = onNavigateToLeft,
                        onNavigateToRightLongPress = onNavigateToRight
                    )
                }

                CallControlSettings()

                if (state.hasStemConfigCapability) {
                    NavigationButton(
                        onClick = onNavigateToCameraRemote,
                        name = stringResource(R.string.camera_remote),
                        description = stringResource(R.string.camera_control_description),
                        title = stringResource(R.string.camera_control)
                    )
                }

                AudioSettings(
                    backdrop = backdrop,
                    hasAdaptiveVolume = true,
                    hasConversationAwareness = true,
                    hasLoudSoundReduction = true,
                    hasAdaptiveAudio = true,
                    onNavigateToAdaptiveStrength = onNavigateToAdaptiveAudio
                )

                ConnectionSettings(
                    backdrop = backdrop
                )

                MicrophoneSettings()

                if (state.hasSleepDetectionCapability) {
                    StyledToggle(
                        label = stringResource(R.string.sleep_detection),
                        isChecked = false,
                        onCheckedChange = {},
                    )
                }

                if (state.hasHeadGesturesCapability) {
                    NavigationButton(
                        name = stringResource(R.string.head_gestures),
                        onClick = onNavigateToHeadGestures,
                        currentState = if (state.isHeadGesturesEnabled) stringResource(R.string.on) else stringResource(
                            R.string.off
                        )
                    )
                }

                NavigationButton(
                    onClick = onNavigateToAccessibility,
                    name = stringResource(R.string.accessibility)
                )

                if (state.hasLoudSoundReductionCapability) {
                    StyledToggle(
                        isChecked = false,
                        onCheckedChange = {},
                        label = stringResource(R.string.off_listening_mode),
                        description = stringResource(R.string.off_listening_mode_description),
                    )
                }

                AboutCard(
                    displayName = state.name,
                    modelNumber = state.model,
                    serialNumbers = listOf("A", "B", "C"),
                    version = state.version,
                    onNavigateToVersionInfo = onNavigateToVersion
                )

                NavigationButton(
                    name = "Debug",
                    onClick = onNavigateToDebug
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .layerBackdrop(backdrop)
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.airpods_not_connected),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.airpods_not_connected_description),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(32.dp))
                LiquidButton(
                    onClick = { onNavigateToTroubleshooting() },
                    backdrop = rememberLayerBackdrop(),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = stringResource(R.string.troubleshooting),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(Modifier.height(16.dp))
                LiquidButton(
                    onClick = {
                        onReconnectToLastDevice()
                    },
                    backdrop = rememberLayerBackdrop(),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = stringResource(R.string.reconnect_to_last_device),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        var shouldShowConfirmationDialog by remember { mutableStateOf(false) }

        if (shouldShowConfirmationDialog) {
            val context = LocalContext.current

            ConfirmationDialog(
                title = stringResource(R.string.support_librepods),
                message = stringResource(R.string.support_dialog_description),
                confirmText = stringResource(R.string.support_me) + " \uDBC0\uDEB5",
                dismissText = stringResource(R.string.never_show_again),
                onConfirm = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        "https://github.com/sponsors/kavishdevar".toUri()
                    )
                    context.startActivity(intent)
                    onDonationDialogShown()
                },
                onDismissRequest = {
                    onDonationDialogShown()
                },
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun HomeScreenLightPreview() {
    LibrePodsTheme {
        HomeContent(
            state = HomeUiState(
                hasHearingHealthCapability = true,
                hasListeningModeCapability = true,
                hasStemConfigCapability = true,
                hasSleepDetectionCapability = true,
                hasHeadGesturesCapability = true,
                hasLoudSoundReductionCapability = true,
                isHeadGesturesEnabled = true
            ),
            onNavigateToSettings = {},
            onNavigateToHearingProtection = {},
            onNavigateToHearingAid = {},
            onNavigateToLeft = {},
            onNavigateToRight = {},
            onNavigateToCameraRemote = {},
            onNavigateToAdaptiveAudio = {},
            onNavigateToHeadGestures = {},
            onNavigateToAccessibility = {},
            onNavigateToVersion = {},
            onNavigateToDebug = {},
            onNavigateToTroubleshooting = {},
            onDonationDialogShown = {},
            onReconnectToLastDevice = {},
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenDarkPreview() {
    LibrePodsTheme {
        HomeContent(
            state = HomeUiState(
                hasHearingHealthCapability = true,
                hasListeningModeCapability = true,
                hasStemConfigCapability = true,
                hasSleepDetectionCapability = true,
                hasHeadGesturesCapability = true,
                hasLoudSoundReductionCapability = true,
                isHeadGesturesEnabled = true
            ),
            onNavigateToSettings = {},
            onNavigateToHearingProtection = {},
            onNavigateToHearingAid = {},
            onNavigateToLeft = {},
            onNavigateToRight = {},
            onNavigateToCameraRemote = {},
            onNavigateToAdaptiveAudio = {},
            onNavigateToHeadGestures = {},
            onNavigateToAccessibility = {},
            onNavigateToVersion = {},
            onNavigateToDebug = {},
            onNavigateToTroubleshooting = {},
            onDonationDialogShown = {},
            onReconnectToLastDevice = {},
        )
    }
}
