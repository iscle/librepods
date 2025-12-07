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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme

@Composable
fun HearingHealthSettings(
    hasPpeCapability: Boolean,
    onNavigateToHearingProtection: () -> Unit,
    onNavigateToHearingAid: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFFFFFFF)

    if (hasPpeCapability) {
        Column {
            SectionTitle(
                title = stringResource(R.string.hearing_health),
            )

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .fillMaxWidth()
                    .background(backgroundColor, RoundedCornerShape(28.dp))
                    .padding(top = 2.dp)
            ) {
                NavigationButton(
                    name = stringResource(R.string.hearing_protection),
                    onClick = onNavigateToHearingProtection,
                    independent = false
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0x40888888),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                )

                NavigationButton(
                    name = stringResource(R.string.hearing_aid),
                    onClick = onNavigateToHearingAid,
                    independent = false
                )
            }
        }
    } else {
        NavigationButton(
            name = stringResource(R.string.hearing_aid),
            onClick = onNavigateToHearingAid
        )
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun HearingHealthSettingsLightPreview() {
    LibrePodsTheme {
        Surface {
            HearingHealthSettings(
                hasPpeCapability = true,
                onNavigateToHearingProtection = {},
                onNavigateToHearingAid = {}
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HearingHealthSettingsDarkPreview() {
    LibrePodsTheme {
        Surface {
            HearingHealthSettings(
                hasPpeCapability = true,
                onNavigateToHearingProtection = {},
                onNavigateToHearingAid = {}
            )
        }
    }
}
