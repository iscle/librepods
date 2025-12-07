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

package me.kavishdevar.librepods.presentation.ui.component

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.data.constants.StemAction
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme

@Composable
fun PressAndHoldSettings(
    onNavigateToLeftLongPress: () -> Unit,
    onNavigateToRightLongPress: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    if (isDarkTheme) Color.White else Color.Black
    val dividerColor = Color(0x40888888)

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    val leftAction = sharedPreferences.getString(
        "left_long_press_action",
        StemAction.CYCLE_NOISE_CONTROL_MODES.name
    )
    val rightAction = sharedPreferences.getString(
        "right_long_press_action",
        StemAction.CYCLE_NOISE_CONTROL_MODES.name
    )

    val leftActionText =
        when (StemAction.valueOf(leftAction ?: StemAction.CYCLE_NOISE_CONTROL_MODES.name)) {
            StemAction.CYCLE_NOISE_CONTROL_MODES -> stringResource(R.string.noise_control)
            StemAction.DIGITAL_ASSISTANT -> "Digital Assistant"
            else -> "INVALID!!"
        }

    val rightActionText =
        when (StemAction.valueOf(rightAction ?: StemAction.CYCLE_NOISE_CONTROL_MODES.name)) {
            StemAction.CYCLE_NOISE_CONTROL_MODES -> stringResource(R.string.noise_control)
            StemAction.DIGITAL_ASSISTANT -> "Digital Assistant"
            else -> "INVALID!!"
        }

    Column {
        SectionTitle(
            title = stringResource(R.string.press_and_hold_airpods),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFFFFFFF),
                    RoundedCornerShape(28.dp)
                )
                .clip(RoundedCornerShape(28.dp))
        ) {
            NavigationButton(
                name = stringResource(R.string.left),
                onClick = onNavigateToLeftLongPress,
                independent = false,
                currentState = leftActionText,
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = dividerColor,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            NavigationButton(
                name = stringResource(R.string.right),
                onClick = onNavigateToRightLongPress,
                independent = false,
                currentState = rightActionText,
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun PressAndHoldSettingsLightPreview() {
    LibrePodsTheme {
        Surface {
            PressAndHoldSettings(
                onNavigateToLeftLongPress = {},
                onNavigateToRightLongPress = {}
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PressAndHoldSettingsDarkPreview() {
    LibrePodsTheme {
        Surface {
            PressAndHoldSettings(
                onNavigateToLeftLongPress = {},
                onNavigateToRightLongPress = {}
            )
        }
    }
}
