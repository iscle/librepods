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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme

@Composable
fun AboutCard(
    displayName: String,
    modelNumber: String,
    serialNumbers: List<String>,
    version: String,
    onNavigateToVersionInfo: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val backgroundColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFFFFFFF)

    Column {
        SectionTitle(
            title = stringResource(R.string.about),
        )

        val rowHeight = remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(28.dp))
                .padding(top = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .onGloballyPositioned { coordinates ->
                        rowHeight.value = with(density) { coordinates.size.height.toDp() }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.model_name),
                    fontSize = 16.sp,
                    color = textColor
                )
                Text(
                    text = displayName,
                    fontSize = 16.sp,
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.Black.copy(
                        alpha = 0.8f
                    )
                )
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0x40888888),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.model_number),
                    fontSize = 16.sp,
                    color = textColor
                )
                Text(
                    text = modelNumber,
                    fontSize = 16.sp,
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.Black.copy(
                        alpha = 0.8f
                    )
                )
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0x40888888),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
            val serialNumber = remember { mutableStateOf(0) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.serial_number),
                    fontSize = 16.sp,
                    color = textColor
                )
                Text(
                    text = serialNumbers[serialNumber.value],
                    fontSize = 16.sp,
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.Black.copy(
                        alpha = 0.8f
                    ),
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            serialNumber.value = (serialNumber.value + 1) % serialNumbers.size
                        }
                )
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0x40888888),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            )
            NavigationButton(
                name = stringResource(R.string.version),
                onClick = onNavigateToVersionInfo,
                currentState = version,
                independent = false,
                height = rowHeight.value + 32.dp
            )
        }
    }
}

@Preview
@Composable
fun AboutCardPreview() {
    LibrePodsTheme {
        Surface {
            AboutCard(
                displayName = "AirPods Pro",
                modelNumber = "A1234",
                serialNumbers = listOf("1234567890", "0987654321"),
                version = "1.0.0",
                onNavigateToVersionInfo = {}
            )
        }
    }
}
