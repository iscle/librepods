package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.TransparencySettings
import me.kavishdevar.librepods.domain.repository.DeviceRepository
import me.kavishdevar.librepods.domain.repository.SettingsRepository

class UpdateTransparencySettingsUseCase(
    private val settingsRepository: SettingsRepository,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(params: TransparencySettings): Boolean {
        // TODO: validation
        settingsRepository.saveTransparencySettings(params)
        return deviceRepository.applyTransparency(params)
    }
}

