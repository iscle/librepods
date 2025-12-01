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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.ui.component.StyledScaffold
import me.kavishdevar.librepods.composables.StyledSlider
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
fun HearingAidAdjustmentsScreen() {
    isSystemInDarkTheme()
    val verticalScrollState = rememberScrollState()
    val hazeState = remember { HazeState() }
    val attManager = ServiceManager.getService()?.attManager ?: throw IllegalStateException("ATTManager not available")

    val aacpManager = remember { ServiceManager.getService()?.aacpManager }
    val backdrop = rememberLayerBackdrop()
    StyledScaffold(
        topBar = {
            StyledTopAppBar(
                title = {
                    Text(stringResource(R.string.adjustments))
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

            var amplificationSliderValue by remember { mutableFloatStateOf(0.5f) }
            var balanceSliderValue by remember { mutableFloatStateOf(0.5f) }
            var toneSliderValue by remember { mutableFloatStateOf(0.5f) }
            var ambientNoiseReductionSliderValue by remember { mutableFloatStateOf(0.0f) }
            var conversationBoostEnabled by remember { mutableStateOf(false) }
            var eq by remember { mutableStateOf(FloatArray(8)) }
            var ownVoiceAmplification by remember { mutableFloatStateOf(0.5f) }

            var phoneMediaEQ by remember { mutableStateOf(FloatArray(8) { 0.5f }) }
            var phoneEQEnabled by remember { mutableStateOf(false) }
            var mediaEQEnabled by remember { mutableStateOf(false) }

            var initialLoadComplete by remember { mutableStateOf(false) }

            var initialReadSucceeded by remember { mutableStateOf(false) }
            var initialReadAttempts by remember { mutableIntStateOf(0) }

            var hearingAidSettings by remember {
                mutableStateOf(
                    HearingAidSettings(
                        leftEQ = eq,
                        rightEQ = eq,
                        leftAmplification = amplificationSliderValue + (0.5f - balanceSliderValue) * amplificationSliderValue * 2,
                        rightAmplification = amplificationSliderValue + (balanceSliderValue - 0.5f) * amplificationSliderValue * 2,
                        leftTone = toneSliderValue,
                        rightTone = toneSliderValue,
                        leftConversationBoost = conversationBoostEnabled,
                        rightConversationBoost = conversationBoostEnabled,
                        leftAmbientNoiseReduction = ambientNoiseReductionSliderValue,
                        rightAmbientNoiseReduction = ambientNoiseReductionSliderValue,
                        netAmplification = amplificationSliderValue,
                        balance = balanceSliderValue,
                        ownVoiceAmplification = ownVoiceAmplification
                    )
                )
            }

            var hearingAidEnabled by remember {
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
                            hearingAidEnabled = (aidStatus?.value?.getOrNull(1) == 0x01.toByte()) && (assistStatus?.value?.getOrNull(0) == 0x01.toByte())
                        }
                    }
                }
            }

            val hearingAidATTListener = remember {
                object : (ByteArray) -> Unit {
                    override fun invoke(value: ByteArray) {
                        val parsed = parseHearingAidSettingsResponse(value)
                        if (parsed != null) {
                            amplificationSliderValue = parsed.netAmplification
                            balanceSliderValue = parsed.balance
                            toneSliderValue = parsed.leftTone
                            ambientNoiseReductionSliderValue = parsed.leftAmbientNoiseReduction
                            conversationBoostEnabled = parsed.leftConversationBoost
                            eq = parsed.leftEQ.copyOf()
                            ownVoiceAmplification = parsed.ownVoiceAmplification
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

            LaunchedEffect(amplificationSliderValue, balanceSliderValue, toneSliderValue, conversationBoostEnabled, ambientNoiseReductionSliderValue, ownVoiceAmplification, initialLoadComplete, initialReadSucceeded) {
                if (!initialLoadComplete) {
                    Timber.d("Initial device load not complete - skipping send")
                    return@LaunchedEffect
                }

                if (!initialReadSucceeded) {
                    Timber.d("Initial device read not successful yet - skipping send until read succeeds")
                    return@LaunchedEffect
                }

                hearingAidSettings = HearingAidSettings(
                    leftEQ = eq,
                    rightEQ = eq,
                    leftAmplification = amplificationSliderValue + if (balanceSliderValue < 0) -balanceSliderValue else 0f,
                    rightAmplification = amplificationSliderValue + if (balanceSliderValue > 0) balanceSliderValue else 0f,
                    leftTone = toneSliderValue,
                    rightTone = toneSliderValue,
                    leftConversationBoost = conversationBoostEnabled,
                    rightConversationBoost = conversationBoostEnabled,
                    leftAmbientNoiseReduction = ambientNoiseReductionSliderValue,
                    rightAmbientNoiseReduction = ambientNoiseReductionSliderValue,
                    netAmplification = amplificationSliderValue,
                    balance = balanceSliderValue,
                    ownVoiceAmplification = ownVoiceAmplification
                )
                Timber.d("Updated settings: ${hearingAidSettings}")
                sendHearingAidSettings(attManager, hearingAidSettings, debounceJob)
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
                                eq = aacpEQ.copyOf()
                                phoneMediaEQ = aacpEQ.copyOf()
                                phoneEQEnabled = aacpManager.eqOnPhone
                                mediaEQEnabled = aacpManager.eqOnMedia
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
                        initialReadAttempts = attempt
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
                        amplificationSliderValue = parsedSettings.netAmplification
                        balanceSliderValue = parsedSettings.balance
                        toneSliderValue = parsedSettings.leftTone
                        ambientNoiseReductionSliderValue = parsedSettings.leftAmbientNoiseReduction
                        conversationBoostEnabled = parsedSettings.leftConversationBoost
                        eq = parsedSettings.leftEQ.copyOf()
                        ownVoiceAmplification = parsedSettings.ownVoiceAmplification
                        initialReadSucceeded = true
                    } else {
                        Timber.d("Failed to read/parse initial hearing aid settings after ${initialReadAttempts} attempts")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    initialLoadComplete = true
                }
            }

            StyledSlider(
                label = stringResource(R.string.amplification),
                valueRange = -1f..1f,
                value = amplificationSliderValue,
                onValueChange = {
                    amplificationSliderValue = it
                },
                startIcon = "􀊥",
                endIcon = "􀊩",
                independent = true,
            )


//            StyledToggle(
//                label = stringResource(R.string.swipe_to_control_amplification),
//                controlCommandIdentifier = AACPManager.Companion.ControlCommandIdentifiers.HPS_GAIN_SWIPE,
//                description = stringResource(R.string.swipe_amplification_description)
//            )

            StyledSlider(
                label = stringResource(R.string.balance),
                valueRange = -1f..1f,
                value = balanceSliderValue,
                onValueChange = {
                    balanceSliderValue = it
                },
                snapPoints = listOf(-1f, 0f, 1f),
                startLabel = stringResource(R.string.left),
                endLabel = stringResource(R.string.right),
                independent = true,
            )

            StyledSlider(
                label = stringResource(R.string.tone),
                valueRange = -1f..1f,
                value = toneSliderValue,
                onValueChange = {
                    toneSliderValue = it
                },
                startLabel = stringResource(R.string.darker),
                endLabel = stringResource(R.string.brighter),
                independent = true,
            )

            StyledSlider(
                label = stringResource(R.string.ambient_noise_reduction),
                valueRange = 0f..1f,
                value = ambientNoiseReductionSliderValue,
                onValueChange = {
                    ambientNoiseReductionSliderValue = it
                },
                startLabel = stringResource(R.string.less),
                endLabel = stringResource(R.string.more),
                independent = true,
            )

//            StyledToggle(
//                label = stringResource(R.string.conversation_boost),
//                checkedState = conversationBoostEnabled,
//                independent = true,
//                description = stringResource(R.string.conversation_boost_description)
//            )
        }
    }
}
