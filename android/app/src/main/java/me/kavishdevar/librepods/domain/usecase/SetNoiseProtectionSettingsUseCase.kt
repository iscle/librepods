package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.DeviceRepository

class SetNoiseProtectionSettingsUseCase(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(params: Map<String, Any>) {
        // TODO: validate and apply
    }
}

