package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.DeviceRepository

class SetHeadTrackingEnabledUseCase(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(params: Boolean): Boolean =
        deviceRepository.setHeadTrackingEnabled(params)
}

