package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.AccessibilitySettings
import me.kavishdevar.librepods.domain.repository.SettingsRepository

class UpdateAccessibilitySettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(params: AccessibilitySettings) {
        settingsRepository.saveAccessibilitySettings(params)
    }
}

