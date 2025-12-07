package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.DeviceRepository

class CalibrateHeadTrackingUseCase(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Boolean = deviceRepository.calibrateHeadTracking()
}

