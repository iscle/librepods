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

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.ui.component.LiquidButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidIconButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidScaffold
import me.kavishdevar.librepods.presentation.ui.component.LiquidTopAppBar
import me.kavishdevar.librepods.presentation.ui.component.StyledSlider
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme

@Composable
fun AdaptiveStrengthScreen(
    onNavigateBack: () -> Unit,

    ) {
    AdaptiveStrengthContent(
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun AdaptiveStrengthContent(
    onNavigateBack: () -> Unit,
) {
    var sliderValue by remember { mutableFloatStateOf(0f) }

    LiquidScaffold(
        topBar = {
            LiquidTopAppBar(
                title = {
                    Text(stringResource(R.string.customize_adaptive_audio))
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
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            Spacer(Modifier.height(spacerHeight))
            StyledSlider(
                label = stringResource(R.string.customize_adaptive_audio),
                value = sliderValue,
                onValueChange = {
                    sliderValue = it
                },
                valueRange = 0f..100f,
                snapPoints = listOf(0f, 50f, 100f),
                startIcon = "􀊥",
                endIcon = "􀊩",
                independent = true,
                description = stringResource(R.string.adaptive_audio_description)
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AdaptiveStrengthScreenLightPreview() {
    LibrePodsTheme {
        AdaptiveStrengthContent(
            onNavigateBack = {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AdaptiveStrengthScreenDarkPreview() {
    LibrePodsTheme {
        AdaptiveStrengthContent(
            onNavigateBack = {}
        )
    }
}
