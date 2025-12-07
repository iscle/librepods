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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme

@Composable
fun NavigationButton(
    name: String,
    onClick: () -> Unit,
    independent: Boolean = true, // indicates if this is part of a group
    title: String? = null,
    description: String? = null,
    currentState: String? = null,
    height: Dp = 58.dp,
) {
    val isDarkTheme = isSystemInDarkTheme()
    var backgroundColor by remember {
        mutableStateOf(
            if (isDarkTheme) Color(0xFF1C1C1E) else Color(
                0xFFFFFFFF
            )
        )
    }

    Column {
        if (title != null) {
            SectionTitle(
                title = title,
            )
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(if (independent) 28.dp else 0.dp))
                .height(height)
                .background(backgroundColor)
                .clickable {
                    onClick()
                }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                color = if (isDarkTheme) Color.White else Color.Black
            )

            if (currentState != null) {
                Text(
                    text = currentState,
                    modifier = Modifier.padding(end = 6.dp),
                    fontSize = 16.sp,
                    color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.Black.copy(
                        alpha = 0.8f
                    )
                )
            }

            Text(
                text = "\uDBC2\uDFFB", // chevron symbol
                fontSize = 16.sp,
                color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f),
            )
        }

        if (description != null) {
            Text(
                text = description,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = if (isDarkTheme) Color.White.copy(alpha = 0.6f) else Color.Black.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun NavigationButtonPreview() {
    LibrePodsTheme {
        Surface {
            NavigationButton(
                title = "Title",
                name = "Name",
                onClick = {},
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun NavigationButtonDarkPreview() {
    LibrePodsTheme {
        Surface {
            NavigationButton(
                title = "Title",
                name = "Name",
                onClick = {},
            )
        }
    }
}
