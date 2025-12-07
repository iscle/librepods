package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.DeviceRepository

class ResetDeviceSettingsUseCase(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke() {
        // TODO: coordinate reset sequence when available
    }
}

