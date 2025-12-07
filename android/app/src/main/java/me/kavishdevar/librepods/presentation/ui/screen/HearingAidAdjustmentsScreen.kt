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

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.Job
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.ui.component.LiquidButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidIconButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidScaffold
import me.kavishdevar.librepods.presentation.ui.component.LiquidTopAppBar
import me.kavishdevar.librepods.presentation.ui.component.StyledSlider
import me.kavishdevar.librepods.util.HearingAidSettings

private var debounceJob: MutableState<Job?> = mutableStateOf(null)
private const val TAG = "HearingAidAdjustments"

@SuppressLint("DefaultLocale")
@Composable
fun HearingAidAdjustmentsScreen(
    onNavigateBack: () -> Unit,
) {
    val verticalScrollState = rememberScrollState()

    LiquidScaffold(
        topBar = {
            LiquidTopAppBar(
                title = {
                    Text(stringResource(R.string.adjustments))
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
            var amplificationSliderValue by remember { mutableFloatStateOf(0.5f) }
            var balanceSliderValue by remember { mutableFloatStateOf(0.5f) }
            var toneSliderValue by remember { mutableFloatStateOf(0.5f) }
            var ambientNoiseReductionSliderValue by remember { mutableFloatStateOf(0.0f) }
            var conversationBoostEnabled by remember { mutableStateOf(false) }
            var eq by remember { mutableStateOf(FloatArray(8)) }
            var ownVoiceAmplification by remember { mutableFloatStateOf(0.5f) }

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
