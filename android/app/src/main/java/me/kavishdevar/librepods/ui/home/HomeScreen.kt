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

package me.kavishdevar.librepods.ui.home

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
import dev.chrisbanes.haze.hazeSource
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.composables.AboutCard
import me.kavishdevar.librepods.composables.AudioSettings
import me.kavishdevar.librepods.composables.BatteryView
import me.kavishdevar.librepods.composables.CallControlSettings
import me.kavishdevar.librepods.composables.ConfirmationDialog
import me.kavishdevar.librepods.composables.ConnectionSettings
import me.kavishdevar.librepods.composables.HearingHealthSettings
import me.kavishdevar.librepods.composables.MicrophoneSettings
import me.kavishdevar.librepods.composables.NavigationButton
import me.kavishdevar.librepods.composables.NoiseControlSettings
import me.kavishdevar.librepods.composables.PressAndHoldSettings
import me.kavishdevar.librepods.composables.StyledButton
import me.kavishdevar.librepods.composables.StyledToggle
import me.kavishdevar.librepods.domain.model.AirPodsModel
import me.kavishdevar.librepods.ui.component.LiquidIconButton
import me.kavishdevar.librepods.ui.component.StyledScaffold
import me.kavishdevar.librepods.ui.component.StyledTopAppBar
import me.kavishdevar.librepods.ui.theme.LibrePodsTheme

@Composable
fun HomeScreen(
    onNavigateToTroubleshooting: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val state by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        onDonationDialogShown = {
            homeViewModel.setDonationDialogShown()
        },
        onNavigateToTroubleshooting = onNavigateToTroubleshooting,
        onReconnectToLastDevice = {
            homeViewModel.reconnectToLastDevice()
        },
    )
}

@Composable
fun HomeContent(
    state: HomeUiState,
    onDonationDialogShown: () -> Unit,
    onNavigateToTroubleshooting: () -> Unit,
    onReconnectToLastDevice: () -> Unit,
) {
    val deviceName = "AirPods Pro"
    val isConnected = true

    StyledScaffold(
        topBar = {
            StyledTopAppBar(
                title = {
                    Text(deviceName)
                },
                actions = {
                    LiquidIconButton(
                        onClick = {},
                        backdrop = backdrop
                    ) {
                        Text("\uDBC0\uDF5F")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (isConnected) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .hazeSource(hazeState)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                BatteryView(
                    model = AirPodsModel.AIRPODS_PRO_1
                )

                Spacer(Modifier.height(32.dp))

                NavigationButton(
                    onClick = {
                        // TOOD: Navigate to rename
                    },
                    name = stringResource(R.string.name),
                    currentState = deviceName,
                    independent = true
                )

                if (state.hasHearingHealthCapability) {
                    Spacer(Modifier.height(32.dp))
                    HearingHealthSettings(
                        onNavigateToHearingProtection = {},
                        onNavigateToHearingAid = {}
                    )
                }

                if (state.hasListeningModeCapability) {
                    Spacer(Modifier.height(16.dp))
                    NoiseControlSettings()
                }

                if (state.hasStemConfigCapability) {
                    Spacer(Modifier.height(16.dp))
                    PressAndHoldSettings(
                        onNavigateToLeftLongPress = {},
                        onNavigateToRightLongPress = {}
                    )
                }

                Spacer(Modifier.height(16.dp))
                CallControlSettings(hazeState = hazeState)

                if (state.hasStemConfigCapability) {
                    Spacer(Modifier.height(16.dp))
                    NavigationButton(
                        onClick = {},
                        name = stringResource(R.string.camera_remote),
                        description = stringResource(R.string.camera_control_description),
                        title = stringResource(R.string.camera_control)
                    )
                }

                Spacer(Modifier.height(16.dp))
                AudioSettings(
                    backdrop = backdrop,
                    hasAdaptiveVolume = true,
                    hasConversationAwareness = true,
                    hasLoudSoundReduction = true,
                    hasAdaptiveAudio = true,
                    onNavigateToAdaptiveStrength = {}
                )

                Spacer(Modifier.height(16.dp))
                ConnectionSettings(
                    backdrop = backdrop
                )

                Spacer(Modifier.height(16.dp))
                MicrophoneSettings(hazeState)

                if (state.hasSleepDetectionCapability) {
                    Spacer(Modifier.height(16.dp))
                    StyledToggle(
                        label = stringResource(R.string.sleep_detection),
                        isChecked = false,
                        onCheckedChange = {},
                        backdrop = backdrop,
                    )
                }

                if (state.hasHeadGesturesCapability) {
                    Spacer(Modifier.height(16.dp))
                    NavigationButton(
                        name = stringResource(R.string.head_gestures),
                        onClick = {},
                        currentState = if (state.isHeadGesturesEnabled) stringResource(R.string.on) else stringResource(R.string.off))
                }

                Spacer(Modifier.height(16.dp))
                NavigationButton(
                    onClick = {},
                    name = stringResource(R.string.accessibility)
                )

                if (state.hasLoudSoundReductionCapability){
                    Spacer(Modifier.height(16.dp))
                    StyledToggle(
                        isChecked = false,
                        onCheckedChange = {},
                        label = stringResource(R.string.off_listening_mode),
                        description = stringResource(R.string.off_listening_mode_description),
                        backdrop = backdrop
                    )
                }

                Spacer(Modifier.height(32.dp))
                AboutCard(
                    displayName = "Display name",
                    modelNumber = "Model number",
                    serialNumbers = listOf("A", "B", "C"),
                    version = "Version",
                    onNavigateToVersionInfo = {}
                )

                Spacer(Modifier.height(16.dp))
                NavigationButton("Debug", {  })
                Spacer(Modifier.height(24.dp))
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
//                    .drawBackdrop(
//                        backdrop = rememberLayerBackdrop(),
//                        exportedBackdrop = backdrop,
//                        shape = { RoundedCornerShape(0.dp) },
//                        highlight = {
//                            Highlight.Ambient.copy(alpha = 0f)
//                        }
//                    )
                    .hazeSource(hazeState)
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
                StyledButton(
                    onClick = { onNavigateToTroubleshooting() },
                    backdrop = backdrop,
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
                StyledButton(
                    onClick = {
                        onReconnectToLastDevice()
                    },
                    backdrop = backdrop,
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

        val context = LocalContext.current
        var shouldShowConfirmationDialog by remember { mutableStateOf(false) }

        if (shouldShowConfirmationDialog) {
            ConfirmationDialog(
                title = stringResource(R.string.support_librepods),
                message = stringResource(R.string.support_dialog_description),
                confirmText = stringResource(R.string.support_me) + " \uDBC0\uDEB5",
                dismissText = stringResource(R.string.never_show_again),
                onConfirm = {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        "https://github.com/sponsors/kavishdevar".toUri()
                    )
                    context.startActivity(browserIntent)
                    onDonationDialogShown()
                },
                onDismissRequest = {
                    onDonationDialogShown()
                },
                hazeState = hazeState,
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
            state = HomeUiState(),
            onDonationDialogShown = {},
            onNavigateToTroubleshooting = {},
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
            state = HomeUiState(),
            onDonationDialogShown = {},
            onNavigateToTroubleshooting = {},
            onReconnectToLastDevice = {},
        )
    }
}
