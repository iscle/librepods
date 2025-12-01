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
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCoerceIn
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.kavishdevar.librepods.ui.component.LiquidSlider
import me.kavishdevar.librepods.ui.theme.LibrePodsTheme

@Composable
fun StyledSlider(
    label: String? = null,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    backdrop: Backdrop = rememberLayerBackdrop(),
    snapPoints: List<Float> = emptyList(),
    snapThreshold: Float = 0.05f,
    startIcon: String? = null,
    endIcon: String? = null,
    startLabel: String? = null,
    endLabel: String? = null,
    independent: Boolean = false,
    description: String? = null
) {
    val backgroundColor = if (isSystemInDarkTheme()) Color(0xFF1C1C1E) else Color(0xFFFFFFFF)
    val isLightTheme = !isSystemInDarkTheme()
    val accentColor =
        if (isLightTheme) Color(0xFF0088FF)
        else Color(0xFF0091FF)
    val trackColor =
        if (isLightTheme) Color(0xFF787878).copy(0.2f)
        else Color(0xFF787880).copy(0.36f)
    val labelTextColor = if (isLightTheme) Color.Black else Color.White

    val sliderBackdrop = rememberLayerBackdrop()
    val trackWidthState = remember { mutableFloatStateOf(0f) }
    val startIconWidthState = remember { mutableFloatStateOf(0f) }
    val endIconWidthState = remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current

    val content = @Composable {
        Box(
            Modifier
                .fillMaxWidth(if (startIcon == null && endIcon == null) 0.95f else 1f)
        ) {
            Box(
                Modifier
                    .padding(vertical = 4.dp)
                    .layerBackdrop(sliderBackdrop)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (startLabel != null || endLabel != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = startLabel ?: "",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = labelTextColor
                            )
                            Text(
                                text = endLabel ?: "",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = labelTextColor
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .then(if (startIcon == null && endIcon == null) Modifier.padding(horizontal = 8.dp) else Modifier),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            if (startIcon != null) {
                                Text(
                                    text = startIcon,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = accentColor,
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .onGloballyPositioned {
                                            startIconWidthState.floatValue = it.size.width.toFloat()
                                        }
                                )
                            }
                            LiquidSlider(
                                value = { value },
                                onValueChange = { onValueChange(it) },
                                valueRange = valueRange,
                                visibilityThreshold = snapThreshold,
                                backdrop = backdrop,
                                modifier = Modifier.weight(1f)
                            )
                            if (endIcon != null) {
                                Text(
                                    text = endIcon,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = accentColor,
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .onGloballyPositioned {
                                            endIconWidthState.floatValue = it.size.width.toFloat()
                                        }
                                )
                            }
                        }
                        if (snapPoints.isNotEmpty() && startLabel != null && endLabel != null) Spacer(Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (snapPoints.isNotEmpty()) {
                                val trackWidth = if (startIcon != null && endIcon != null) trackWidthState.floatValue - with(density) { 6.dp.toPx() } * 2 else trackWidthState.floatValue- with(density) { 22.dp.toPx() }
                                val startOffset =
                                    if (startIcon != null) startIconWidthState.floatValue + with(
                                        density
                                    ) { 34.dp.toPx() } else with(density) { 14.dp.toPx() }
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                ) {
                                    snapPoints.forEach { point ->
                                        val pointFraction =
                                            ((point - valueRange.start) / (valueRange.endInclusive - valueRange.start))
                                                .fastCoerceIn(0f, 1f)
                                        Box(
                                            Modifier
                                                .graphicsLayer {
                                                    translationX =
                                                        startOffset + pointFraction * trackWidth - 4.dp.toPx()
                                                }
                                                .size(2.dp)
                                                .background(
                                                    trackColor,
                                                    CircleShape
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (independent) {

        Column (
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            if (label != null) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = labelTextColor.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor, RoundedCornerShape(28.dp))
                    .padding(horizontal = 8.dp, vertical = 0.dp)
                    .heightIn(min = 58.dp),
                contentAlignment = Alignment.Center
            ) {
                content()
            }

            if (description != null) {
                Text(
                    text = description,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = (if (isSystemInDarkTheme()) Color.White else Color.Black).copy(alpha = 0.6f),
                    modifier = Modifier
                        .padding(horizontal = 18.dp, vertical = 4.dp)
                )
            }
        }
    } else {
        if (label != null) Log.w("StyledSlider", "Label is ignored when independent is false")
        if (description != null) Log.w("StyledSlider", "Description is ignored when independent is false")
        content()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun StyledSliderPreview() {
    LibrePodsTheme {
        Surface {
            StyledSlider(
                value = 0.5f,
                onValueChange = {},
                valueRange = 0f..2f,
                snapPoints = listOf(1f),
                snapThreshold = 0.1f,
                independent = true,
                startIcon = "A",
                endIcon = "B",
            )
        }
    }
}
