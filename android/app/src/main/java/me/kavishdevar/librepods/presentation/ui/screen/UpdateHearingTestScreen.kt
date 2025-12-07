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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.Job
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.service.ServiceManager
import me.kavishdevar.librepods.presentation.ui.component.LiquidButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidIconButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidScaffold
import me.kavishdevar.librepods.presentation.ui.component.LiquidTopAppBar
import me.kavishdevar.librepods.util.AACPManager
import me.kavishdevar.librepods.util.HearingAidSettings

private var debounceJob: MutableState<Job?> = mutableStateOf(null)
private const val TAG = "HearingAidAdjustments"

@Composable
fun UpdateHearingTestScreen(
    onNavigateBack: () -> Unit,
) {
    val verticalScrollState = rememberScrollState()

    if (true) {
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

    LiquidScaffold(
        topBar = {
            LiquidTopAppBar(
                title = {
                    Text(stringResource(R.string.hearing_test))
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
                .verticalScroll(verticalScrollState)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.hearing_test_value_instruction),
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            val conversationBoostEnabled = remember { mutableStateOf(false) }
            val leftEQ = remember { mutableStateOf(FloatArray(8)) }
            val rightEQ = remember { mutableStateOf(FloatArray(8)) }

            remember { mutableStateOf(false) }
            remember { mutableStateOf(false) }
            remember { mutableIntStateOf(0) }

            remember {
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

            remember {
                val aidStatus =
                    aacpManager?.controlCommandStatusList?.find { it.identifier == AACPManager.Companion.ControlCommandIdentifiers.HEARING_AID }
                val assistStatus =
                    aacpManager?.controlCommandStatusList?.find { it.identifier == AACPManager.Companion.ControlCommandIdentifiers.HEARING_ASSIST_CONFIG }
                mutableStateOf(
                    (aidStatus?.value?.getOrNull(1) == 0x01.toByte()) && (assistStatus?.value?.getOrNull(
                        0
                    ) == 0x01.toByte())
                )
            }

            val frequencies =
                listOf("250Hz", "500Hz", "1kHz", "2kHz", "3kHz", "4kHz", "6kHz", "8kHz")

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
