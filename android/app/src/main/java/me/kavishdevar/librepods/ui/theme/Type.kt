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

package me.kavishdevar.librepods.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import me.kavishdevar.librepods.R

private val SfProFontFamily = FontFamily(
    Font(R.font.sf_pro)
)

// MARK: iOS Text Styles (1:1 with iOS)
// These match Apple's Human Interface Guidelines exactly

// Large Title (iOS Navigation Bar Large Title)
val iosLargeTitle = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 34.sp,
    lineHeight = 41.sp,
    letterSpacing = 0.37.sp
)

// Title 1
val iosTitle1 = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 28.sp,
    lineHeight = 34.sp,
    letterSpacing = 0.36.sp
)

// Title 2
val iosTitle2 = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    letterSpacing = 0.35.sp
)

// Title 3
val iosTitle3 = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    lineHeight = 25.sp,
    letterSpacing = 0.38.sp
)

// Headline (iOS Navigation Bar Regular/Inline Title)
val iosHeadline = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.41).sp
)

// Body
val iosBody = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 17.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.41).sp
)

// Callout
val iosCallout = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    lineHeight = 21.sp,
    letterSpacing = (-0.32).sp
)

// Subheadline
val iosSubheadline = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    lineHeight = 20.sp,
    letterSpacing = (-0.24).sp
)

// Footnote
val iosFootnote = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    lineHeight = 18.sp,
    letterSpacing = (-0.08).sp
)

// Caption 1
val iosCaption1 = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.sp
)

// Caption 2
val iosCaption2 = TextStyle(
    fontFamily = SfProFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp,
    lineHeight = 13.sp,
    letterSpacing = 0.07.sp
)

// MARK: Material Design Typography mapped from iOS styles
val IosTypography = Typography(
    // Display styles — mapped to iOS Large Titles
    displayLarge = iosLargeTitle,
    displayMedium = iosTitle1,
    displaySmall = iosTitle2,

    // Headline styles — mapped to iOS Titles
    headlineLarge = iosTitle1,
    headlineMedium = iosTitle2,
    headlineSmall = iosTitle3,

    // Title styles — iOS Headline for TopAppBar (titleLarge), then smaller titles
    titleLarge = iosHeadline,  // Used by TopAppBar - matches iOS Navigation Bar
    titleMedium = iosTitle3,
    titleSmall = iosSubheadline,

    // Body styles — iOS Body text
    bodyLarge = iosBody,
    bodyMedium = iosCallout,
    bodySmall = iosSubheadline,

    // Label styles — iOS small text
    labelLarge = iosFootnote,
    labelMedium = iosCaption1,
    labelSmall = iosCaption2
)
