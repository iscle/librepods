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

package me.kavishdevar.librepods.presentation.ui.accessibility

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.layerBackdrop
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.ui.component.LiquidIconButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidScaffold
import me.kavishdevar.librepods.presentation.ui.component.LiquidTopAppBar
import me.kavishdevar.librepods.presentation.ui.component.NavigationButton
import me.kavishdevar.librepods.presentation.ui.component.StyledDropdown
import me.kavishdevar.librepods.presentation.ui.component.StyledSlider
import me.kavishdevar.librepods.presentation.ui.component.StyledToggle
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme

@Composable
fun AccessibilitySettingsScreen(
    onNavigateToTransparencyCustomization: () -> Unit,
) {
    AccessibilitySettingsContent(
        onNavigateToTransparencyCustomization = onNavigateToTransparencyCustomization
    )
}

@Composable
fun AccessibilitySettingsContent(
    onNavigateToTransparencyCustomization: () -> Unit,
) {
    val hasLoudSoundReductionCapability = true
    val hasSwipeForVolumeCapability = true

    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    var isSdpOffsetAvailable by remember { mutableStateOf(true) } // TODO

    var hearingAidEnabled by remember { mutableStateOf(true) } // TODO

    LiquidScaffold(
        topBar = {
            LiquidTopAppBar(
                title = {
                    Text(stringResource(R.string.accessibility))
                },
                navigationIcon = {
                    LiquidIconButton(
                        onClick = {},
                        backdrop = backdrop,
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
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                val backgroundColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color(0xFFFFFFFF)

                var phoneEQEnabled by remember { mutableStateOf(false) }
                var mediaEQEnabled by remember { mutableStateOf(false) }

                val pressSpeedOptions = mapOf(
                    0.toByte() to stringResource(R.string.default_option),
                    1.toByte() to stringResource(R.string.slower),
                    2.toByte() to stringResource(R.string.slowest)
                )
                val selectedPressSpeedValue = 0.toByte() // TODO
                var selectedPressSpeed by remember {
                    mutableStateOf(
                        pressSpeedOptions[selectedPressSpeedValue] ?: pressSpeedOptions[0]
                    )
                }

                val pressAndHoldDurationOptions = mapOf(
                    0.toByte() to stringResource(R.string.default_option),
                    1.toByte() to stringResource(R.string.slower),
                    2.toByte() to stringResource(R.string.slowest)
                )
                val selectedPressAndHoldDurationValue = 0.toByte() // TODO
                var selectedPressAndHoldDuration by remember {
                    mutableStateOf(
                        pressAndHoldDurationOptions[selectedPressAndHoldDurationValue]
                            ?: pressAndHoldDurationOptions[0]
                    )
                }

                val volumeSwipeSpeedOptions = mapOf(
                    1.toByte() to stringResource(R.string.default_option),
                    2.toByte() to stringResource(R.string.longer),
                    3.toByte() to stringResource(R.string.longest)
                )
                val selectedVolumeSwipeSpeedValue = 0.toByte() // TODO
                var selectedVolumeSwipeSpeed by remember {
                    mutableStateOf(
                        volumeSwipeSpeedOptions[selectedVolumeSwipeSpeedValue]
                            ?: volumeSwipeSpeedOptions[1]
                    )
                }

                var toneVolumeValue by remember { mutableFloatStateOf(75f) }

                DropdownMenuComponent(
                    label = stringResource(R.string.press_speed),
                    description = stringResource(R.string.press_speed_description),
                    options = pressSpeedOptions.values.toList(),
                    selectedOption = selectedPressSpeed ?: stringResource(R.string.default_option),
                    onOptionSelected = { newValue ->
                        selectedPressSpeed = newValue
                    },
                    textColor = textColor,
                    independent = true
                )

                DropdownMenuComponent(
                    label = stringResource(R.string.press_and_hold_duration),
                    description = stringResource(R.string.press_and_hold_duration_description),
                    options = pressAndHoldDurationOptions.values.toList(),
                    selectedOption = selectedPressAndHoldDuration
                        ?: stringResource(R.string.default_option),
                    onOptionSelected = { newValue ->
                        selectedPressAndHoldDuration = newValue
                    },
                    textColor = textColor,
                    independent = true
                )

                StyledToggle(
                    title = stringResource(R.string.noise_control),
                    label = stringResource(R.string.noise_cancellation_single_airpod),
                    description = stringResource(R.string.noise_cancellation_single_airpod_description),
                    isChecked = true,
                    onCheckedChange = {},
                    independent = true,
                )

                if (hasLoudSoundReductionCapability) {
                    StyledToggle(
                        label = stringResource(R.string.loud_sound_reduction),
                        description = stringResource(R.string.loud_sound_reduction_description),
                        isChecked = true,
                        onCheckedChange = {},
                    )
                }

                if (!hearingAidEnabled && isSdpOffsetAvailable) {
                    NavigationButton(
                        name = stringResource(R.string.customize_transparency_mode),
                        onClick = onNavigateToTransparencyCustomization,
                    )
                }

                StyledSlider(
                    label = stringResource(R.string.tone_volume),
                    description = stringResource(R.string.tone_volume_description),
                    value = toneVolumeValue,
                    onValueChange = {
                        toneVolumeValue = it
                    },
                    valueRange = 0f..100f,
                    snapPoints = listOf(75f),
                    startIcon = "\uDBC0\uDEA1",
                    endIcon = "\uDBC0\uDEA9",
                    independent = true
                )

                if (hasSwipeForVolumeCapability) {
                    StyledToggle(
                        label = stringResource(R.string.volume_control),
                        description = stringResource(R.string.volume_control_description),
                        isChecked = false,
                        onCheckedChange = {},
                    )

                    DropdownMenuComponent(
                        label = stringResource(R.string.volume_swipe_speed),
                        description = stringResource(R.string.volume_swipe_speed_description),
                        options = volumeSwipeSpeedOptions.values.toList(),
                        selectedOption = selectedVolumeSwipeSpeed
                            ?: stringResource(R.string.default_option),
                        onOptionSelected = { newValue ->
                            selectedVolumeSwipeSpeed = newValue
                        },
                        textColor = textColor,
                        independent = true
                    )
                }

                if (!hearingAidEnabled && isSdpOffsetAvailable) {
                    Text(
                        text = stringResource(R.string.apply_eq_to),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor.copy(alpha = 0.6f),
                        modifier = Modifier.padding(8.dp, bottom = 0.dp)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backgroundColor, RoundedCornerShape(28.dp))
                            .padding(vertical = 0.dp)
                    ) {
                        val darkModeLocal = isSystemInDarkTheme()

                        val phoneShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                        var phoneBackgroundColor by remember {
                            mutableStateOf(
                                if (darkModeLocal) Color(
                                    0xFF1C1C1E
                                ) else Color(0xFFFFFFFF)
                            )
                        }
                        val phoneAnimatedBackgroundColor by animateColorAsState(
                            targetValue = phoneBackgroundColor,
                            animationSpec = tween(durationMillis = 500)
                        )

                        Row(
                            modifier = Modifier
                                .height(48.dp)
                                .fillMaxWidth()
                                .background(phoneAnimatedBackgroundColor, phoneShape)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            phoneBackgroundColor =
                                                if (darkModeLocal) Color(0x40888888) else Color(
                                                    0x40D9D9D9
                                                )
                                            tryAwaitRelease()
                                            phoneBackgroundColor =
                                                if (darkModeLocal) Color(0xFF1C1C1E) else Color(
                                                    0xFFFFFFFF
                                                )
                                            phoneEQEnabled = !phoneEQEnabled
                                        }
                                    )
                                }
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.phone),
                                fontSize = 16.sp,
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )
                            Checkbox(
                                checked = phoneEQEnabled,
                                onCheckedChange = { phoneEQEnabled = it },
                                colors = CheckboxDefaults.colors().copy(
                                    checkedCheckmarkColor = Color(0xFF007AFF),
                                    uncheckedCheckmarkColor = Color.Transparent,
                                    checkedBoxColor = Color.Transparent,
                                    uncheckedBoxColor = Color.Transparent,
                                    checkedBorderColor = Color.Transparent,
                                    uncheckedBorderColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .height(24.dp)
                                    .scale(1.5f)
                            )
                        }

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color(0x40888888)
                        )

                        val mediaShape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                        var mediaBackgroundColor by remember {
                            mutableStateOf(
                                if (darkModeLocal) Color(
                                    0xFF1C1C1E
                                ) else Color(0xFFFFFFFF)
                            )
                        }
                        val mediaAnimatedBackgroundColor by animateColorAsState(
                            targetValue = mediaBackgroundColor,
                            animationSpec = tween(durationMillis = 500)
                        )

                        Row(
                            modifier = Modifier
                                .height(48.dp)
                                .fillMaxWidth()
                                .background(mediaAnimatedBackgroundColor, mediaShape)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            mediaBackgroundColor =
                                                if (darkModeLocal) Color(0x40888888) else Color(
                                                    0x40D9D9D9
                                                )
                                            tryAwaitRelease()
                                            mediaBackgroundColor =
                                                if (darkModeLocal) Color(0xFF1C1C1E) else Color(
                                                    0xFFFFFFFF
                                                )
                                            mediaEQEnabled = !mediaEQEnabled
                                        }
                                    )
                                }
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.media),
                                fontSize = 16.sp,
                                color = textColor,
                                modifier = Modifier.weight(1f)
                            )
                            Checkbox(
                                checked = mediaEQEnabled,
                                onCheckedChange = { mediaEQEnabled = it },
                                colors = CheckboxDefaults.colors().copy(
                                    checkedCheckmarkColor = Color(0xFF007AFF),
                                    uncheckedCheckmarkColor = Color.Transparent,
                                    checkedBoxColor = Color.Transparent,
                                    uncheckedBoxColor = Color.Transparent,
                                    checkedBorderColor = Color.Transparent,
                                    uncheckedBorderColor = Color.Transparent
                                ),
                                modifier = Modifier
                                    .height(24.dp)
                                    .scale(1.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DropdownMenuComponent(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    textColor: Color,
    description: String? = null,
    independent: Boolean = true
) {
    val density = LocalDensity.current
    val itemHeightPx = with(density) { 48.dp.toPx() }

    var expanded by remember { mutableStateOf(false) }
    var touchOffset by remember { mutableStateOf<Offset?>(null) }
    var boxPosition by remember { mutableStateOf(Offset.Zero) }
    var lastDismissTime by remember { mutableLongStateOf(0L) }
    var parentHoveredIndex by remember { mutableStateOf<Int?>(null) }
    var parentDragActive by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (independent) {
                        if (description != null) {
                            Modifier.padding(top = 8.dp, bottom = 4.dp)
                        } else {
                            Modifier.padding(vertical = 8.dp)
                        }
                    } else Modifier
                )
                .background(
                    if (independent) (if (isSystemInDarkTheme()) Color(0xFF1C1C1E) else Color(
                        0xFFFFFFFF
                    )) else Color.Transparent,
                    if (independent) RoundedCornerShape(28.dp) else RoundedCornerShape(0.dp)
                )
                then (
                if (independent) Modifier.padding(horizontal = 4.dp) else Modifier
                )
                .clip(if (independent) RoundedCornerShape(28.dp) else RoundedCornerShape(0.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp)
                    .height(58.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val now = System.currentTimeMillis()
                            if (expanded) {
                                expanded = false
                                lastDismissTime = now
                            } else {
                                if (now - lastDismissTime > 250L) {
                                    touchOffset = offset
                                    expanded = true
                                }
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset ->
                                val now = System.currentTimeMillis()
                                touchOffset = offset
                                if (!expanded && now - lastDismissTime > 250L) {
                                    expanded = true
                                }
                                lastDismissTime = now
                                parentDragActive = true
                                parentHoveredIndex = 0
                            },
                            onDrag = { change, _ ->
                                val current = change.position
                                val touch = touchOffset ?: current
                                val posInPopupY = current.y - touch.y
                                val idx = (posInPopupY / itemHeightPx).toInt()
                                parentHoveredIndex = idx
                            },
                            onDragEnd = {
                                parentDragActive = false
                                parentHoveredIndex?.let { idx ->
                                    if (idx in options.indices) {
                                        onOptionSelected(options[idx])
                                        expanded = false
                                        lastDismissTime = System.currentTimeMillis()
                                    }
                                }
                                parentHoveredIndex = null
                            },
                            onDragCancel = {
                                parentDragActive = false
                                parentHoveredIndex = null
                            }
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = label,
                        fontSize = 16.sp,
                        color = textColor,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    if (!independent && description != null) {
                        Text(
                            text = description,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                            color = textColor.copy(alpha = 0.6f),
                            modifier = Modifier.padding(16.dp, top = 0.dp, bottom = 2.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        boxPosition = coordinates.positionInParent()
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedOption,
                            fontSize = 16.sp,
                            color = textColor.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "􀆏",
                            fontSize = 16.sp,
                            color = textColor.copy(alpha = 0.6f),
                            modifier = Modifier
                                .padding(start = 6.dp)
                        )
                    }

                    StyledDropdown(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                            lastDismissTime = System.currentTimeMillis()
                        },
                        options = options,
                        selectedOption = selectedOption,
                        touchOffset = touchOffset,
                        boxPosition = boxPosition,
                        externalHoveredIndex = parentHoveredIndex,
                        externalDragActive = parentDragActive,
                        onOptionSelected = { option ->
                            onOptionSelected(option)
                            expanded = false
                        },
                    )
                }
            }
        }
        if (independent && description != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = (if (isSystemInDarkTheme()) Color.White else Color.Black).copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AccessibilitySettingsScreenLightPreview() {
    LibrePodsTheme {
        AccessibilitySettingsContent(
            onNavigateToTransparencyCustomization = {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AccessibilitySettingsScreenDarkPreview() {
    LibrePodsTheme {
        AccessibilitySettingsContent(
            onNavigateToTransparencyCustomization = {}
        )
    }
}
