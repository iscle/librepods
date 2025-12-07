package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.FirmwareRepository

class CheckForFirmwareUpdatesUseCase(
    private val firmwareRepository: FirmwareRepository
) {
    suspend operator fun invoke(): Boolean = firmwareRepository.hasUpdateAvailable()
}

