package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.AccessibilitySettings
import me.kavishdevar.librepods.domain.repository.SettingsRepository

class GetAccessibilitySettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): AccessibilitySettings =
        settingsRepository.getAccessibilitySettings()
}

