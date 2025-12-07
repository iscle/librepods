package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.AppSettings
import me.kavishdevar.librepods.domain.repository.SettingsRepository

class UpdateAppSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(params: AppSettings) {
        // TODO: validation and downstream sync if needed
        settingsRepository.saveAppSettings(params)
    }
}

