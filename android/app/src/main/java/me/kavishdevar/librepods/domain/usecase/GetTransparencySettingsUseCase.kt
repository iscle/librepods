package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.TransparencySettings
import me.kavishdevar.librepods.domain.repository.SettingsRepository

class GetTransparencySettingsUseCase(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(): TransparencySettings =
        settingsRepository.getTransparencySettings()
}

