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

package me.kavishdevar.librepods.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.ui.theme.LibrePodsTheme

@Composable
fun AudioSettings(
    backdrop: Backdrop,
    hasAdaptiveVolume: Boolean,
    hasConversationAwareness: Boolean,
    hasLoudSoundReduction: Boolean,
    hasAdaptiveAudio: Boolean,
    onNavigateToAdaptiveStrength: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()

    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ){
        Text(
            text = stringResource(R.string.audio),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = LocalTextStyle.current.color.copy(alpha = 0.6f)
        )
    }

    val backgroundColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFFFFFFF)

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(28.dp))
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(28.dp))
            .padding(top = 2.dp)
    ) {

        if (hasAdaptiveVolume) {
            StyledToggle(
                label = stringResource(R.string.personalized_volume),
                description = stringResource(R.string.personalized_volume_description),
                isChecked = false,
                onCheckedChange = {},
                independent = false,
                backdrop = backdrop
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0x40888888),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
        }

        if (hasConversationAwareness) {
            StyledToggle(
                label = stringResource(R.string.conversational_awareness),
                description = stringResource(R.string.conversational_awareness_description),
                isChecked = false,
                onCheckedChange = {},
                independent = false,
                backdrop = backdrop
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0x40888888),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
        }

        if (hasLoudSoundReduction){
            StyledToggle(
                label = stringResource(R.string.loud_sound_reduction),
                description = stringResource(R.string.loud_sound_reduction_description),
                isChecked = false,
                onCheckedChange = {},
                independent = false,
                backdrop = backdrop
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0x40888888),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
        }

        if (hasAdaptiveAudio) {
            NavigationButton(
                name = stringResource(R.string.adaptive_audio),
                onClick = onNavigateToAdaptiveStrength,
                independent = false
            )
        }
    }
}

@Preview
@Composable
fun AudioSettingsPreview() {
    LibrePodsTheme {
        Surface {
            AudioSettings(
                hasAdaptiveVolume = true,
                hasConversationAwareness = true,
                hasLoudSoundReduction = true,
                hasAdaptiveAudio = true,
                onNavigateToAdaptiveStrength = {},
                backdrop = rememberLayerBackdrop()
            )
        }
    }
}
