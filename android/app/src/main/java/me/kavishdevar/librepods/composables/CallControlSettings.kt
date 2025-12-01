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

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import dev.chrisbanes.haze.HazeState
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.services.ServiceManager
import me.kavishdevar.librepods.ui.theme.LibrePodsTheme
import me.kavishdevar.librepods.utils.AACPManager
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun CallControlSettings(
    hazeState: HazeState
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ){
        Text(
            text = stringResource(R.string.call_controls),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = LocalTextStyle.current.color.copy(alpha = 0.6f)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .padding(top = 2.dp)
    ) {
        val pressOnceText = stringResource(R.string.press_once)
        val pressTwiceText = stringResource(R.string.press_twice)

        var flipped by remember { mutableStateOf(false) }
        var singlePressAction by remember { mutableStateOf(if (flipped) pressTwiceText else pressOnceText) }
        var doublePressAction by remember { mutableStateOf(if (flipped) pressOnceText else pressTwiceText) }

        var showSinglePressDropdown by remember { mutableStateOf(false) }
        var touchOffsetSingle by remember { mutableStateOf<Offset?>(null) }
        var boxPositionSingle by remember { mutableStateOf(Offset.Zero) }
        var lastDismissTimeSingle by remember { mutableLongStateOf(0L) }
        var parentHoveredIndexSingle by remember { mutableStateOf<Int?>(null) }
        var parentDragActiveSingle by remember { mutableStateOf(false) }

        var showDoublePressDropdown by remember { mutableStateOf(false) }
        var touchOffsetDouble by remember { mutableStateOf<Offset?>(null) }
        var boxPositionDouble by remember { mutableStateOf(Offset.Zero) }
        var lastDismissTimeDouble by remember { mutableLongStateOf(0L) }
        var parentHoveredIndexDouble by remember { mutableStateOf<Int?>(null) }
        var parentDragActiveDouble by remember { mutableStateOf(false) }

        val density = LocalDensity.current
        val itemHeightPx = with(density) { 48.dp.toPx() }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(58.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.answer_call),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = stringResource(R.string.press_once),
                    fontSize = 16.sp,
                    color = LocalTextStyle.current.color.copy(alpha = 0.6f)
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
                    .padding(horizontal = 16.dp)
                    .height(58.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val now = System.currentTimeMillis()
                            if (showSinglePressDropdown) {
                                showSinglePressDropdown = false
                                lastDismissTimeSingle = now
                            } else {
                                if (now - lastDismissTimeSingle > 250L) {
                                    touchOffsetSingle = offset
                                    showSinglePressDropdown = true
                                }
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset ->
                                val now = System.currentTimeMillis()
                                touchOffsetSingle = offset
                                if (!showSinglePressDropdown && now - lastDismissTimeSingle > 250L) {
                                    showSinglePressDropdown = true
                                }
                                lastDismissTimeSingle = now
                                parentDragActiveSingle = true
                                parentHoveredIndexSingle = 0
                            },
                            onDrag = { change, _ ->
                                val current = change.position
                                val touch = touchOffsetSingle ?: current
                                val posInPopupY = current.y - touch.y
                                val idx = (posInPopupY / itemHeightPx).toInt()
                                parentHoveredIndexSingle = idx
                            },
                            onDragEnd = {
                                parentDragActiveSingle = false
                                parentHoveredIndexSingle?.let { idx ->
                                    val options = listOf(pressOnceText, pressTwiceText)
                                    if (idx in options.indices) {
                                        val option = options[idx]
                                        singlePressAction = option
                                        doublePressAction =
                                            if (option == pressOnceText) pressTwiceText else pressOnceText
                                        showSinglePressDropdown = false
                                        lastDismissTimeSingle = System.currentTimeMillis()
                                    }
                                }
                                parentHoveredIndexSingle = null
                            },
                            onDragCancel = {
                                parentDragActiveSingle = false
                                parentHoveredIndexSingle = null
                            }
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.mute_unmute),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        boxPositionSingle = coordinates.positionInParent()
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = singlePressAction,
                            fontSize = 16.sp,
                            color = LocalTextStyle.current.color.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "􀆏",
                            fontSize = 16.sp,
                            color = LocalTextStyle.current.color.copy(alpha = 0.6f),
                            modifier = Modifier
                                .padding(start = 6.dp)
                        )
                    }

                    StyledDropdown(
                        expanded = showSinglePressDropdown,
                        onDismissRequest = {
                            showSinglePressDropdown = false
                            lastDismissTimeSingle = System.currentTimeMillis()
                        },
                        options = listOf(pressOnceText, pressTwiceText),
                        selectedOption = singlePressAction,
                        touchOffset = touchOffsetSingle,
                        boxPosition = boxPositionSingle,
                        externalHoveredIndex = parentHoveredIndexSingle,
                        externalDragActive = parentDragActiveSingle,
                        onOptionSelected = { option ->
                            singlePressAction = option
                            doublePressAction =
                                if (option == pressOnceText) pressTwiceText else pressOnceText
                            showSinglePressDropdown = false
                        },
                        hazeState = hazeState
                    )
                }
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
                    .padding(horizontal = 16.dp)
                    .height(58.dp)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val now = System.currentTimeMillis()
                            if (showDoublePressDropdown) {
                                showDoublePressDropdown = false
                                lastDismissTimeDouble = now
                            } else {
                                if (now - lastDismissTimeDouble > 250L) {
                                    touchOffsetDouble = offset
                                    showDoublePressDropdown = true
                                }
                            }
                        }
                    }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { offset ->
                                val now = System.currentTimeMillis()
                                touchOffsetDouble = offset
                                if (!showDoublePressDropdown && now - lastDismissTimeDouble > 250L) {
                                    showDoublePressDropdown = true
                                }
                                lastDismissTimeDouble = now
                                parentDragActiveDouble = true
                                parentHoveredIndexDouble = 0
                            },
                            onDrag = { change, _ ->
                                val current = change.position
                                val touch = touchOffsetDouble ?: current
                                val posInPopupY = current.y - touch.y
                                val idx = (posInPopupY / itemHeightPx).toInt()
                                parentHoveredIndexDouble = idx
                            },
                            onDragEnd = {
                                parentDragActiveDouble = false
                                parentHoveredIndexDouble?.let { idx ->
                                    val options = listOf(pressOnceText, pressTwiceText)
                                    if (idx in options.indices) {
                                        val option = options[idx]
                                        doublePressAction = option
                                        singlePressAction =
                                            if (option == pressOnceText) pressTwiceText else pressOnceText
                                        showDoublePressDropdown = false
                                        lastDismissTimeDouble = System.currentTimeMillis()
                                    }
                                }
                                parentHoveredIndexDouble = null
                            },
                            onDragCancel = {
                                parentDragActiveDouble = false
                                parentHoveredIndexDouble = null
                            }
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.hang_up),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        boxPositionDouble = coordinates.positionInParent()
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = doublePressAction,
                            fontSize = 16.sp,
                            color = LocalTextStyle.current.color.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "􀆏",
                            fontSize = 16.sp,
                            color = LocalTextStyle.current.color.copy(alpha = 0.6f),
                            modifier = Modifier
                                .padding(start = 6.dp)
                        )
                    }

                    StyledDropdown(
                        expanded = showDoublePressDropdown,
                        onDismissRequest = {
                            showDoublePressDropdown = false
                            lastDismissTimeDouble = System.currentTimeMillis()
                        },
                        options = listOf(pressOnceText, pressTwiceText),
                        selectedOption = doublePressAction,
                        touchOffset = touchOffsetDouble,
                        boxPosition = boxPositionDouble,
                        externalHoveredIndex = parentHoveredIndexDouble,
                        externalDragActive = parentDragActiveDouble,
                        onOptionSelected = { option ->
                            doublePressAction = option
                            singlePressAction =
                                if (option == pressOnceText) pressTwiceText else pressOnceText
                            showDoublePressDropdown = false
                        },
                        hazeState = hazeState
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CallControlSettingsPreview() {
    LibrePodsTheme {
        Surface {
            CallControlSettings(HazeState())
        }
    }
}
