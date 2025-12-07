package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.SettingsRepository

class GetNoiseProtectionSettingsUseCase(
    private val settingsRepository: SettingsRepository
) { // Placeholder
    suspend operator fun invoke(): Map<String, Any> = emptyMap()
}

