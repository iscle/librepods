package me.kavishdevar.librepods.domain.model

// Core device state

data class DeviceState(
    val connected: Boolean,
    val batteryLeft: Int?,
    val batteryRight: Int?,
    val firmwareVersion: String?
)

// Bud side indicator

enum class BudSide { LEFT, RIGHT }

// Hearing domain

data class HearingProfile(
    val leftGainDb: Float,
    val rightGainDb: Float,
)

// Transparency / ANC settings

data class TransparencySettings(
    val level: Int,
    val ambientEnhancement: Boolean,
)

// Accessibility settings

data class AccessibilitySettings(
    val highContrast: Boolean,
    val largeText: Boolean,
)

// App settings

data class AppSettings(
    val onboardingCompleted: Boolean,
    val debugEnabled: Boolean,
)

// App version info

data class VersionInfo(
    val appVersion: String,
    val latestAvailable: String?,
)
