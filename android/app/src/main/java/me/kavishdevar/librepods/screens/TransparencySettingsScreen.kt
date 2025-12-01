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
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.delay
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.ui.component.StyledScaffold
import me.kavishdevar.librepods.composables.StyledSlider
import me.kavishdevar.librepods.services.ServiceManager
import me.kavishdevar.librepods.ui.component.StyledTopAppBar
import me.kavishdevar.librepods.utils.ATTHandles
import me.kavishdevar.librepods.utils.RadareOffsetFinder
import me.kavishdevar.librepods.utils.TransparencySettings
import me.kavishdevar.librepods.utils.parseTransparencySettingsResponse
import me.kavishdevar.librepods.utils.sendTransparencySettings
import timber.log.Timber
import java.io.IOException
import kotlin.io.encoding.ExperimentalEncodingApi

private const val TAG = "TransparencySettings"

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun TransparencySettingsScreen() {
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val verticalScrollState = rememberScrollState()
    val attManager = ServiceManager.getService()?.attManager ?: return
    val aacpManager = remember { ServiceManager.getService()?.aacpManager }
    var isSdpOffsetAvailable by
        remember { mutableStateOf(RadareOffsetFinder.isSdpOffsetAvailable()) }

    val trackColor = if (isDarkTheme) Color(0xFFB3B3B3) else Color(0xFF929491)
    val activeTrackColor = if (isDarkTheme) Color(0xFF007AFF) else Color(0xFF3C6DF5)
    val thumbColor = if (isDarkTheme) Color(0xFFFFFFFF) else Color(0xFFFFFFFF)

    val backdrop = rememberLayerBackdrop()

    StyledScaffold(
        topBar = {
            StyledTopAppBar(
                title = {
                    Text(stringResource(R.string.customize_transparency_mode))
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .hazeSource(hazeState)
                .layerBackdrop(backdrop)
                .fillMaxSize()
                .verticalScroll(verticalScrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            Spacer(Modifier.height(spacerHeight))
            val backgroundColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFFFFFFF)

            var enabled by remember { mutableStateOf(false) }
            var amplificationSliderValue by remember { mutableFloatStateOf(0.5f) }
            var balanceSliderValue by remember { mutableFloatStateOf(0.5f) }
            var toneSliderValue by remember { mutableFloatStateOf(0.5f) }
            var ambientNoiseReductionSliderValue by remember { mutableFloatStateOf(0.0f) }
            var conversationBoostEnabled by remember { mutableStateOf(false) }
            var eq by remember { mutableStateOf(FloatArray(8)) }
            var phoneMediaEQ by remember { mutableStateOf(FloatArray(8) { 0.5f }) }

            var initialLoadComplete by remember { mutableStateOf(false) }

            var initialReadSucceeded by remember { mutableStateOf(false) }
            var initialReadAttempts by remember { mutableIntStateOf(0) }

            var transparencySettings by remember {
                mutableStateOf(
                    TransparencySettings(
                        enabled = enabled,
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
                        balance = balanceSliderValue
                    )
                )
            }

            val transparencyListener = remember {
                object : (ByteArray) -> Unit {
                    override fun invoke(value: ByteArray) {
                        val parsed = parseTransparencySettingsResponse(value)
                        enabled = parsed.enabled
                        amplificationSliderValue = parsed.netAmplification
                        balanceSliderValue = parsed.balance
                        toneSliderValue = parsed.leftTone
                        ambientNoiseReductionSliderValue =
                            parsed.leftAmbientNoiseReduction
                        conversationBoostEnabled = parsed.leftConversationBoost
                        eq = parsed.leftEQ.copyOf()
                        Timber.d("Updated transparency settings from notification")
                    }
                }
            }

            LaunchedEffect(
                enabled,
                amplificationSliderValue,
                balanceSliderValue,
                toneSliderValue,
                conversationBoostEnabled,
                ambientNoiseReductionSliderValue,
                eq,
                initialLoadComplete,
                initialReadSucceeded
            ) {
                if (!initialLoadComplete) {
                    Timber.d("Initial device load not complete - skipping send")
                    return@LaunchedEffect
                }

                if (!initialReadSucceeded) {
                    Log.d(
                        TAG,
                        "Initial device read not successful yet - skipping send until read succeeds"
                    )
                    return@LaunchedEffect
                }

                transparencySettings = TransparencySettings(
                    enabled = enabled,
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
                    balance = balanceSliderValue
                )
                Log.d("TransparencySettings", "Updated settings: ${transparencySettings}")
                sendTransparencySettings(attManager, transparencySettings)
            }

            DisposableEffect(Unit) {
                onDispose {
                    attManager.unregisterListener(ATTHandles.TRANSPARENCY, transparencyListener)
                }
            }

            LaunchedEffect(Unit) {
                Timber.d("Connecting to ATT...")
                try {
                    attManager.enableNotifications(ATTHandles.TRANSPARENCY)
                    attManager.registerListener(ATTHandles.TRANSPARENCY, transparencyListener)

                    // If we have an AACP manager, prefer its EQ data to populate EQ controls first
                    try {
                        if (aacpManager != null) {
                            Timber.d("Found AACPManager, reading cached EQ data")
                            val aacpEQ = aacpManager.eqData
                            if (aacpEQ.isNotEmpty()) {
                                eq = aacpEQ.copyOf()
                                phoneMediaEQ = aacpEQ.copyOf()
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

                    var parsedSettings: TransparencySettings? = null
                    for (attempt in 1..3) {
                        initialReadAttempts = attempt
                        try {
                            val data = attManager.read(ATTHandles.TRANSPARENCY)
                            parsedSettings = parseTransparencySettingsResponse(data = data)
                            Timber.d("Parsed settings on attempt $attempt")
                        } catch (e: Exception) {
                            Timber.w("Read attempt $attempt failed: ${e.message}")
                        }
                        delay(200)
                    }

                    if (parsedSettings != null) {
                        Timber.d("Initial transparency settings: $parsedSettings")
                        enabled = parsedSettings.enabled
                        amplificationSliderValue = parsedSettings.netAmplification
                        balanceSliderValue = parsedSettings.balance
                        toneSliderValue = parsedSettings.leftTone
                        ambientNoiseReductionSliderValue =
                            parsedSettings.leftAmbientNoiseReduction
                        conversationBoostEnabled = parsedSettings.leftConversationBoost
                        eq = parsedSettings.leftEQ.copyOf()
                        initialReadSucceeded = true
                    } else {
                        Log.d(
                            TAG,
                            "Failed to read/parse initial transparency settings after ${initialReadAttempts} attempts"
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    initialLoadComplete = true
                }
            }

            // Only show transparency mode section if SDP offset is available
            if (isSdpOffsetAvailable) {
//                StyledToggle(
//                    label = stringResource(R.string.transparency_mode),
//                    checkedState = enabled,
//                    independent = true,
//                    description = stringResource(R.string.customize_transparency_mode_description)
//                )
                Spacer(Modifier.height(4.dp))
                StyledSlider(
                    label = stringResource(R.string.amplification),
                    valueRange = -1f..1f,
                    value = amplificationSliderValue,
                    onValueChange = {
                        amplificationSliderValue = it
                    },
                    startIcon = "􀊥",
                    endIcon = "􀊩",
                    independent = true
                )

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

//                StyledToggle(
//                    label = stringResource(R.string.conversation_boost),
//                    checkedState = conversationBoostEnabled,
//                    independent = true,
//                    description = stringResource(R.string.conversation_boost_description)
//                )
            }

            // Only show transparency mode EQ section if SDP offset is available
            if (isSdpOffsetAvailable) {
                Text(
                    text = stringResource(R.string.equalizer),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor.copy(alpha = 0.6f),
                    modifier = Modifier.padding(16.dp, bottom = 4.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor, RoundedCornerShape(28.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    for (i in 0 until 8) {
                        var eqValue by remember(eq[i]) { mutableFloatStateOf(eq[i]) }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                        ) {
                            Text(
                                text = String.format("%.2f", eqValue),
                                fontSize = 12.sp,
                                color = textColor,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Slider(
                                value = eqValue,
                                onValueChange = { newVal ->
                                    eqValue = newVal
                                    val newEQ = eq.copyOf()
                                    newEQ[i] = eqValue
                                    eq = newEQ
                                },
                                valueRange = 0f..100f,
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(36.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = thumbColor,
                                    activeTrackColor = activeTrackColor,
                                    inactiveTrackColor = trackColor
                                ),
                                thumb = {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .shadow(4.dp, CircleShape)
                                            .background(thumbColor, CircleShape)
                                    )
                                },
                                track = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(12.dp),
                                        contentAlignment = Alignment.CenterStart
                                    )
                                    {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(4.dp)
                                                .background(trackColor, RoundedCornerShape(4.dp))
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(eqValue / 100f)
                                                .height(4.dp)
                                                .background(
                                                    activeTrackColor,
                                                    RoundedCornerShape(4.dp)
                                                )
                                        )
                                    }
                                }
                            )

                            Text(
                                text = stringResource(R.string.band_label, i + 1),
                                fontSize = 12.sp,
                                color = textColor,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
