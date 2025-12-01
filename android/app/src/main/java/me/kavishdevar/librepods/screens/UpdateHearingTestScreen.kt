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

package me.kavishdevar.librepods.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.ui.component.StyledScaffold
import me.kavishdevar.librepods.services.ServiceManager
import me.kavishdevar.librepods.ui.component.StyledTopAppBar
import me.kavishdevar.librepods.utils.AACPManager
import me.kavishdevar.librepods.utils.ATTHandles
import me.kavishdevar.librepods.utils.HearingAidSettings
import me.kavishdevar.librepods.utils.parseHearingAidSettingsResponse
import me.kavishdevar.librepods.utils.sendHearingAidSettings
import timber.log.Timber
import java.io.IOException
import kotlin.io.encoding.ExperimentalEncodingApi

private var debounceJob: MutableState<Job?> = mutableStateOf(null)
private const val TAG = "HearingAidAdjustments"

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun UpdateHearingTestScreen() {
    val verticalScrollState = rememberScrollState()
    val attManager = ServiceManager.getService()?.attManager
    if (attManager == null) {
        Text(
            text = stringResource(R.string.att_manager_is_null_try_reconnecting),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
        return
    }

    val aacpManager = remember { ServiceManager.getService()?.aacpManager }
    val backdrop = rememberLayerBackdrop()
    StyledScaffold(
        topBar = {
            StyledTopAppBar(
                title = {
                    Text(stringResource(R.string.hearing_test))
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .hazeSource(hazeState)
                .fillMaxSize()
                .layerBackdrop(backdrop)
                .verticalScroll(verticalScrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            Spacer(Modifier.height(spacerHeight))

            Text(
                text = stringResource(R.string.hearing_test_value_instruction),
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            val conversationBoostEnabled = remember { mutableStateOf(false) }
            val leftEQ = remember { mutableStateOf(FloatArray(8)) }
            val rightEQ = remember { mutableStateOf(FloatArray(8)) }

            val initialLoadComplete = remember { mutableStateOf(false) }
            val initialReadSucceeded = remember { mutableStateOf(false) }
            val initialReadAttempts = remember { mutableIntStateOf(0) }

            val hearingAidSettings = remember {
                mutableStateOf(
                    HearingAidSettings(
                        leftEQ = leftEQ.value,
                        rightEQ = rightEQ.value,
                        leftAmplification = 0.5f,
                        rightAmplification = 0.5f,
                        leftTone = 0.5f,
                        rightTone = 0.5f,
                        leftConversationBoost = conversationBoostEnabled.value,
                        rightConversationBoost = conversationBoostEnabled.value,
                        leftAmbientNoiseReduction = 0.0f,
                        rightAmbientNoiseReduction = 0.0f,
                        netAmplification = 0.5f,
                        balance = 0.5f,
                        ownVoiceAmplification = 0.5f
                    )
                )
            }

            val hearingAidEnabled = remember {
                val aidStatus = aacpManager?.controlCommandStatusList?.find { it.identifier == AACPManager.Companion.ControlCommandIdentifiers.HEARING_AID }
                val assistStatus = aacpManager?.controlCommandStatusList?.find { it.identifier == AACPManager.Companion.ControlCommandIdentifiers.HEARING_ASSIST_CONFIG }
                mutableStateOf((aidStatus?.value?.getOrNull(1) == 0x01.toByte()) && (assistStatus?.value?.getOrNull(0) == 0x01.toByte()))
            }

            val hearingAidListener = remember {
                object : AACPManager.ControlCommandListener {
                    override fun onControlCommandReceived(controlCommand: AACPManager.ControlCommand) {
                        if (controlCommand.identifier == AACPManager.Companion.ControlCommandIdentifiers.HEARING_AID.value ||
                            controlCommand.identifier == AACPManager.Companion.ControlCommandIdentifiers.HEARING_ASSIST_CONFIG.value) {
                            val aidStatus = aacpManager?.controlCommandStatusList?.find { it.identifier == AACPManager.Companion.ControlCommandIdentifiers.HEARING_AID }
                            val assistStatus = aacpManager?.controlCommandStatusList?.find { it.identifier == AACPManager.Companion.ControlCommandIdentifiers.HEARING_ASSIST_CONFIG }
                            hearingAidEnabled.value = (aidStatus?.value?.getOrNull(1) == 0x01.toByte()) && (assistStatus?.value?.getOrNull(0) == 0x01.toByte())
                        }
                    }
                }
            }

            val hearingAidATTListener = remember {
                object : (ByteArray) -> Unit {
                    override fun invoke(value: ByteArray) {
                        val parsed = parseHearingAidSettingsResponse(value)
                        if (parsed != null) {
                            leftEQ.value = parsed.leftEQ.copyOf()
                            rightEQ.value = parsed.rightEQ.copyOf()
                            conversationBoostEnabled.value = parsed.leftConversationBoost
                            Timber.d("Updated hearing aid settings from notification")
                        } else {
                            Timber.w("Failed to parse hearing aid settings from notification")
                        }
                    }
                }
            }

            LaunchedEffect(Unit) {
                aacpManager?.registerControlCommandListener(AACPManager.Companion.ControlCommandIdentifiers.HEARING_AID, hearingAidListener)
                aacpManager?.registerControlCommandListener(AACPManager.Companion.ControlCommandIdentifiers.HEARING_ASSIST_CONFIG, hearingAidListener)
            }

            DisposableEffect(Unit) {
                onDispose {
                    aacpManager?.unregisterControlCommandListener(AACPManager.Companion.ControlCommandIdentifiers.HEARING_AID, hearingAidListener)
                    aacpManager?.unregisterControlCommandListener(AACPManager.Companion.ControlCommandIdentifiers.HEARING_ASSIST_CONFIG, hearingAidListener)
                    attManager.unregisterListener(ATTHandles.HEARING_AID, hearingAidATTListener)
                }
            }

            LaunchedEffect(leftEQ.value, rightEQ.value, conversationBoostEnabled.value, initialLoadComplete.value, initialReadSucceeded.value) {
                if (!initialLoadComplete.value) {
                    Timber.d("Initial device load not complete - skipping send")
                    return@LaunchedEffect
                }

                if (!initialReadSucceeded.value) {
                    Timber.d("Initial device read not successful yet - skipping send until read succeeds")
                    return@LaunchedEffect
                }

                hearingAidSettings.value = HearingAidSettings(
                    leftEQ = leftEQ.value,
                    rightEQ = rightEQ.value,
                    leftAmplification = 0.5f,
                    rightAmplification = 0.5f,
                    leftTone = 0.5f,
                    rightTone = 0.5f,
                    leftConversationBoost = conversationBoostEnabled.value,
                    rightConversationBoost = conversationBoostEnabled.value,
                    leftAmbientNoiseReduction = 0.0f,
                    rightAmbientNoiseReduction = 0.0f,
                    netAmplification = 0.5f,
                    balance = 0.5f,
                    ownVoiceAmplification = 0.5f
                )
                Timber.d("Updated settings: ${hearingAidSettings.value}")
                sendHearingAidSettings(attManager, hearingAidSettings.value, debounceJob)
            }

            LaunchedEffect(Unit) {
                Timber.d("Connecting to ATT...")
                try {
                    attManager.enableNotifications(ATTHandles.HEARING_AID)
                    attManager.registerListener(ATTHandles.HEARING_AID, hearingAidATTListener)

                    try {
                        if (aacpManager != null) {
                            Timber.d("Found AACPManager, reading cached EQ data")
                            val aacpEQ = aacpManager.eqData
                            if (aacpEQ.isNotEmpty()) {
                                leftEQ.value = aacpEQ.copyOf()
                                rightEQ.value = aacpEQ.copyOf()
                                Timber.d("Populated EQ from AACPManager: ${aacpEQ.toList()}")
                            } else {
                                Timber.d("AACPManager EQ data empty")
                            }
                        } else {
                            Timber.d("No AACPManager available")
                        }
                    } catch (e: Exception) {
                        Timber.w("Error reading EQ from AACPManager: ${e.message}")
                    }

                    var parsedSettings: HearingAidSettings? = null
                    for (attempt in 1..3) {
                        initialReadAttempts.intValue = attempt
                        try {
                            val data = attManager.read(ATTHandles.HEARING_AID)
                            parsedSettings = parseHearingAidSettingsResponse(data = data)
                            if (parsedSettings != null) {
                                Timber.d("Parsed settings on attempt $attempt")
                                break
                            } else {
                                Timber.d("Parsing returned null on attempt $attempt")
                            }
                        } catch (e: Exception) {
                            Timber.w("Read attempt $attempt failed: ${e.message}")
                        }
                        delay(200)
                    }

                    if (parsedSettings != null) {
                        Timber.d("Initial hearing aid settings: $parsedSettings")
                        leftEQ.value = parsedSettings.leftEQ.copyOf()
                        rightEQ.value = parsedSettings.rightEQ.copyOf()
                        conversationBoostEnabled.value = parsedSettings.leftConversationBoost
                        initialReadSucceeded.value = true
                    } else {
                        Timber.d("Failed to read/parse initial hearing aid settings after ${initialReadAttempts.intValue} attempts")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    initialLoadComplete.value = true
                }
            }

            val frequencies = listOf("250Hz", "500Hz", "1kHz", "2kHz", "3kHz", "4kHz", "6kHz", "8kHz")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(Modifier.width(60.dp))
                Text(
                    text = stringResource(R.string.left),
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.right),
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
            }

            frequencies.forEachIndexed { index, freq ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = freq,
                        modifier = Modifier
                            .width(60.dp)
                            .align(Alignment.CenterVertically),
                        textAlign = TextAlign.End,
                        fontSize = 16.sp,
                    )
                    OutlinedTextField(
                        value = leftEQ.value[index].toString(),
                        onValueChange = { newValue ->
                            val parsed = newValue.toFloatOrNull()
                            if (parsed != null) {
                                val newArray = leftEQ.value.copyOf()
                                newArray[index] = parsed
                                leftEQ.value = newArray
                            }
                        },
//                        label = { Text("Value", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.sf_pro))) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = rightEQ.value[index].toString(),
                        onValueChange = { newValue ->
                            val parsed = newValue.toFloatOrNull()
                            if (parsed != null) {
                                val newArray = rightEQ.value.copyOf()
                                newArray[index] = parsed
                                rightEQ.value = newArray
                            }
                        },
//                        label = { Text("Value", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.sf_pro))) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
