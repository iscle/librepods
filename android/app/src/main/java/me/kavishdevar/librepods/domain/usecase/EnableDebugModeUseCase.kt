package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.SettingsRepository

class EnableDebugModeUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(params: Boolean) {
        val current = settingsRepository.getAppSettings()
        settingsRepository.saveAppSettings(current.copy(debugEnabled = params))
    }
}

