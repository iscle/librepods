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

package me.kavishdevar.librepods.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val IosLightColorScheme = ColorScheme(
    primary = Color(0xFF007AFF),           // tintColor / link
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0x00000000),  // iOS doesn’t use primary containers
    onPrimaryContainer = Color(0xFF000000),
    inversePrimary = Color(0xFF007AFF),    // stays same; used on inverseSurface (black)

    secondary = Color(0xFF3C3C43).copy(alpha = 0.6f), // secondaryLabel
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0x00000000),
    onSecondaryContainer = Color(0xFF000000),

    tertiary = Color(0xFF3C3C43).copy(alpha = 0.3f),  // tertiaryLabel
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0x00000000),
    onTertiaryContainer = Color(0xFF000000),

    background = Color(0xFFFFFFFF),        // systemBackground
    onBackground = Color(0xFF000000),     // primary label

    surface = Color(0xFFF2F2F7),           // secondarySystemBackground
    onSurface = Color(0xFF000000),        // primary label on surface

    surfaceVariant = Color(0xFFE5E5EA),   // tertiarySystemBackground
    onSurfaceVariant = Color(0xFF3C3C43), // secondary label

    surfaceTint = Color(0xFF007AFF),

    inverseSurface = Color(0xFF000000),    // systemBackground in dark mode → inverse
    inverseOnSurface = Color(0xFFFFFFFF),

    error = Color(0xFFFF3B30),             // systemRed
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD5),
    onErrorContainer = Color(0xFF5E0F0A),

    outline = Color(0xFFC6C6C8),           // separator / line
    outlineVariant = Color(0xFFE5E5EA),

    scrim = Color(0x52000000),

    // Extended colors: set to reasonable iOS-like defaults
    surfaceBright = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFF2F2F7),
    surfaceContainer = Color(0xFFF2F2F7),
    surfaceContainerLow = Color(0xFFFFFFFF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFE5E5EA),
    surfaceContainerHighest = Color(0xFFD1D1D6),

    // Fixed colors: not used in iOS, but required by Material 3
    primaryFixed = Color(0xFF007AFF),
    primaryFixedDim = Color(0xFFD9ECFF),
    onPrimaryFixed = Color(0xFFFFFFFF),
    onPrimaryFixedVariant = Color(0xFF003678),

    secondaryFixed = Color(0xFFE2E2E7),
    secondaryFixedDim = Color(0xFFD1D1D6),
    onSecondaryFixed = Color(0xFF1D1D1F),
    onSecondaryFixedVariant = Color(0xFF3A3A3C),

    tertiaryFixed = Color(0xFFE2E2E7),
    tertiaryFixedDim = Color(0xFFD1D1D6),
    onTertiaryFixed = Color(0xFF1D1D1F),
    onTertiaryFixedVariant = Color(0xFF3A3A3C)
)

val IosDarkColorScheme = ColorScheme(
    primary = Color(0xFF0A84FF),           // iOS dark mode tintColor (slightly softer than 0984FF)
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0x00000000),
    onPrimaryContainer = Color(0xFFFFFFFF),

    inversePrimary = Color(0xFF007AFF),    // use light-mode tint on white (inverseSurface)

    secondary = Color(0xFF98989D),         // secondaryLabel (lighter than light mode)
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0x00000000),
    onSecondaryContainer = Color(0xFFFFFFFF),

    tertiary = Color(0xFF48484A),          // tertiaryLabel (darker in dark mode)
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0x00000000),
    onTertiaryContainer = Color(0xFFFFFFFF),

    background = Color(0xFF000000),        // systemBackground
    onBackground = Color(0xFFFFFFFF),     // primary label

    surface = Color(0xFF1C1C1E),           // secondarySystemBackground
    onSurface = Color(0xFFFFFFFF),

    surfaceVariant = Color(0xFF2C2C2E),   // tertiarySystemBackground
    onSurfaceVariant = Color(0xFF98989D),

    surfaceTint = Color(0xFF0A84FF),

    inverseSurface = Color(0xFFFFFFFF),
    inverseOnSurface = Color(0xFF000000),

    error = Color(0xFFFF453A),             // systemRed (slightly different in dark mode)
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFF5E0F0A),
    onErrorContainer = Color(0xFFFFDAD5),

    outline = Color(0xFF38383A),           // separator in dark
    outlineVariant = Color(0xFF38383A),

    scrim = Color(0x52000000),

    // Extended
    surfaceBright = Color(0xFF2C2C2E),
    surfaceDim = Color(0xFF000000),
    surfaceContainer = Color(0xFF1C1C1E),
    surfaceContainerLow = Color(0xFF000000),
    surfaceContainerLowest = Color(0xFF000000),
    surfaceContainerHigh = Color(0xFF2C2C2E),
    surfaceContainerHighest = Color(0xFF3A3A3C),

    // Fixed (Material-only, set to reasonable dark mode matches)
    primaryFixed = Color(0xFFA4C8FF),
    primaryFixedDim = Color(0xFF0A84FF),
    onPrimaryFixed = Color(0xFF002D62),
    onPrimaryFixedVariant = Color(0xFFFFFFFF),

    secondaryFixed = Color(0xFF98989D),
    secondaryFixedDim = Color(0xFF545458),
    onSecondaryFixed = Color(0xFFFFFFFF),
    onSecondaryFixedVariant = Color(0xFFE2E2E7),

    tertiaryFixed = Color(0xFF98989D),
    tertiaryFixedDim = Color(0xFF545458),
    onTertiaryFixed = Color(0xFFFFFFFF),
    onTertiaryFixedVariant = Color(0xFFE2E2E7)
)

@Composable
fun LibrePodsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> IosDarkColorScheme
        else -> IosLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = IosTypography,
        content = content
    )
}
