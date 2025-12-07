package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.AppSettings
import me.kavishdevar.librepods.domain.repository.SettingsRepository

class GetAppSettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): AppSettings = settingsRepository.getAppSettings()
}

