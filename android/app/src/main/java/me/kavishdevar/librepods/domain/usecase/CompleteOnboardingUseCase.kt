package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.SettingsRepository

class CompleteOnboardingUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() {
        val current = settingsRepository.getAppSettings()
        settingsRepository.saveAppSettings(current.copy(onboardingCompleted = true))
    }
}

