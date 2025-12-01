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

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.kavishdevar.librepods.ui.component.LiquidToggle
import me.kavishdevar.librepods.ui.theme.LibrePodsTheme

@Composable
fun StyledToggle(
    title: String? = null,
    label: String,
    description: String? = null,
    isChecked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    independent: Boolean = true,
    enabled: Boolean = true,
    backdrop: Backdrop
) {
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    var backgroundColor by remember { mutableStateOf(if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFFFFFFF)) }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(durationMillis = 500)
    )

    if (independent) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            if (title != null) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .background(animatedBackgroundColor, RoundedCornerShape(28.dp))
                    .padding(4.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                backgroundColor = if (isDarkTheme) Color(0x40888888) else Color(0x40D9D9D9)
                                tryAwaitRelease()
                                backgroundColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFFFFFFF)
                            },
                            onTap = {
                                if (enabled) {
                                    onCheckedChange?.invoke(!isChecked)
                                }
                            }
                        )
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = textColor
                    )

                    LiquidToggle(
                        selected = { isChecked },
                        onSelect = {
                            if (enabled) {
                                onCheckedChange?.invoke(!isChecked)
                            }
                        },
                        backdrop = backdrop
                    )
                }
            }
            if (description != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = description,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = textColor.copy(alpha = 0.6f)
                )
            }
        }
    } else {
        val isPressed = remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    shape = RoundedCornerShape(28.dp),
                    color = if (isPressed.value) Color(0xFFE0E0E0) else Color.Transparent
                )
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed.value = true
                            tryAwaitRelease()
                            isPressed.value = false
                        }
                    )
                }
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (enabled) {
                        onCheckedChange?.invoke(!isChecked)
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = textColor
                )
                Spacer(Modifier.height(4.dp))
                if (description != null) {
                    Text(
                        text = description,
                        fontSize = 12.sp,
                        color = textColor.copy(0.6f)
                    )
                }
            }

            LiquidToggle(
                selected = { isChecked },
                onSelect = {
                    if (enabled) {
                        onCheckedChange?.invoke(!isChecked)
                    }
                },
                backdrop = backdrop
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun StyledToggleLightPreview() {
    LibrePodsTheme {
        Surface {
            StyledToggle(
                title = "Example Title",
                label = "Example Toggle",
                description = "This is an example description for the styled toggle.",
                isChecked = true,
                onCheckedChange = {},
                enabled = true,
                independent = true,
                backdrop = rememberLayerBackdrop()
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun StyledToggleDisabledLightPreview() {
    LibrePodsTheme {
        Surface {
            StyledToggle(
                title = "Example Title",
                label = "Example Toggle",
                description = "This is an example description for the styled toggle.",
                isChecked = true,
                onCheckedChange = {},
                enabled = false,
                independent = true,
                backdrop = rememberLayerBackdrop()
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun StyledToggleDarkPreview() {
    LibrePodsTheme {
        Surface {
            StyledToggle(
                title = "Example Title",
                label = "Example Toggle",
                description = "This is an example description for the styled toggle.",
                isChecked = true,
                onCheckedChange = {},
                enabled = true,
                independent = true,
                backdrop = rememberLayerBackdrop()
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun StyledToggleDisabledDarkPreview() {
    LibrePodsTheme {
        Surface {
            StyledToggle(
                title = "Example Title",
                label = "Example Toggle",
                description = "This is an example description for the styled toggle.",
                isChecked = true,
                onCheckedChange = {},
                enabled = false,
                independent = true,
                backdrop = rememberLayerBackdrop()
            )
        }
    }
}
