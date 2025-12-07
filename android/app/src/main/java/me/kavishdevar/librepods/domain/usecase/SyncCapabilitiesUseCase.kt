package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.DeviceRepository

class SyncCapabilitiesUseCase(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke() {
        // TODO: cache capabilities
        deviceRepository.supportsLongPress()
    }
}

