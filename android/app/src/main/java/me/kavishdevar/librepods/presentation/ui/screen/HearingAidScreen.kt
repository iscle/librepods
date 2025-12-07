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

package me.kavishdevar.librepods.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.ui.component.LiquidButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidIconButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidScaffold
import me.kavishdevar.librepods.presentation.ui.component.LiquidTopAppBar
import me.kavishdevar.librepods.presentation.ui.component.NavigationButton


@Composable
fun HearingAidScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAdjustments: () -> Unit, onNavigateToUpdateHearingTest: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val verticalScrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    val showDialog = remember { mutableStateOf(false) }
    remember { mutableStateOf(true) }

    remember {
        mutableStateOf(true)
    }

    LiquidScaffold(
        topBar = {
            LiquidTopAppBar(
                title = {
                    Text(stringResource(R.string.hearing_aid))
                },
                navigationIcon = {
                    LiquidIconButton(
                        onClick = onNavigateBack,
                        backdrop = rememberLayerBackdrop(),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Text("\uDBC2\uDFF6")
                    }
                },
                backdrop = backdrop
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
                .verticalScroll(verticalScrollState)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            Spacer(Modifier.height(spacerHeight))

//            val mediaAssistEnabled = remember { mutableStateOf(false) }
//            val adjustMediaEnabled = remember { mutableStateOf(false) }
//            val adjustPhoneEnabled = remember { mutableStateOf(false) }

//            fun onAdjustPhoneChange(value: Boolean) {
//                // TODO
//            }

//            fun onAdjustMediaChange(value: Boolean) {
//                // TODO
//            }

            Text(
                text = stringResource(R.string.hearing_aid),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor.copy(alpha = 0.6f),
                modifier = Modifier.padding(16.dp, bottom = 2.dp)
            )

            val backgroundColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFFFFFFF)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor, RoundedCornerShape(28.dp))
                    .clip(
                        RoundedCornerShape(28.dp)
                    )
            ) {
//                StyledToggle(
//                    label = stringResource(R.string.hearing_aid),
//                    checkedState = hearingAidEnabled,
//                    independent = false
//                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0x40888888),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )
                NavigationButton(
                    name = stringResource(R.string.adjustments),
                    onClick = onNavigateToAdjustments,
                    independent = false
                )
            }
            Text(
                text = stringResource(R.string.hearing_aid_description),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = (if (isSystemInDarkTheme()) Color.White else Color.Black).copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))

            NavigationButton(
                name = stringResource(R.string.update_hearing_test),
                onClick = onNavigateToUpdateHearingTest,
                independent = true
            )

            // not implemented yet

            // StyledToggle(
            //     title = stringResource(R.string.media_assist),
            //     label = stringResource(R.string.media_assist),
            //     checkedState = mediaAssistEnabled,
            //     independent = true,
            //     description = stringResource(R.string.media_assist_description)
            // )

            // Spacer(Modifier.height(8.dp))

            // Column (
            //     modifier = Modifier
            //         .fillMaxWidth()
            //         .background(backgroundColor, RoundedCornerShape(28.dp))
            // ) {
            //     StyledToggle(
            //         label = stringResource(R.string.adjust_media),
            //         checkedState = adjustMediaEnabled,
            //         onCheckedChange = { onAdjustMediaChange(it) },
            //         independent = false
            //     )
            //     HorizontalDivider(
            //         thickness = 1.dp,
            //         color = Color(0x40888888),
            //         modifier = Modifier
            //             .padding(horizontal = 12.dp)
            //     )

            //     StyledToggle(
            //         label = stringResource(R.string.adjust_calls),
            //         checkedState = adjustPhoneEnabled,
            //         onCheckedChange = { onAdjustPhoneChange(it) },
            //         independent = false
            //     )
            // }
        }
    }

    if (showDialog.value) {
//        ConfirmationDialog(
//            title = "Enable Hearing Aid",
//            message = "Enabling Hearing Aid will disable Headphone Accommodation and Customized Transparency Mode.",
//            confirmText = "Enable",
//            dismissText = "Cancel",
//            onConfirm = {
//                showDialog.value = false
//                val enrolled = aacpManager?.controlCommandStatusList?.find { it.identifier == AACPManager.Companion.ControlCommandIdentifiers.HEARING_AID }?.value?.getOrNull(0) == 0x01.toByte()
//                if (!enrolled) {
//                    aacpManager?.sendControlCommand(AACPManager.Companion.ControlCommandIdentifiers.HEARING_AID.value, byteArrayOf(0x01, 0x01))
//                } else {
//                    aacpManager.sendControlCommand(AACPManager.Companion.ControlCommandIdentifiers.HEARING_AID.value, byteArrayOf(0x01, 0x01))
//                }
//                aacpManager?.sendControlCommand(AACPManager.Companion.ControlCommandIdentifiers.HEARING_ASSIST_CONFIG.value, 0x01.toByte())
//                hearingAidEnabled.value = true
//                CoroutineScope(Dispatchers.IO).launch {
//                    try {
//                        val data = attManager.read(ATTHandles.TRANSPARENCY)
//                        val parsed = parseTransparencySettingsResponse(data)
//                        val disabledSettings = parsed.copy(enabled = false)
//                        sendTransparencySettings(attManager, disabledSettings)
//                    } catch (e: Exception) {
//                        Timber.e("Error disabling transparency: ${e.message}")
//                    }
//                }
//            },
//        )
    }
}
