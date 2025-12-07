package me.kavishdevar.librepods.domain.repository

import me.kavishdevar.librepods.domain.model.AccessibilitySettings
import me.kavishdevar.librepods.domain.model.AppSettings
import me.kavishdevar.librepods.domain.model.TransparencySettings

interface SettingsRepository {
    suspend fun getAppSettings(): AppSettings
    suspend fun saveAppSettings(settings: AppSettings)
    suspend fun getAccessibilitySettings(): AccessibilitySettings
    suspend fun saveAccessibilitySettings(settings: AccessibilitySettings)
    suspend fun getTransparencySettings(): TransparencySettings
    suspend fun saveTransparencySettings(settings: TransparencySettings)
}

