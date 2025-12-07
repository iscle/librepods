package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.DeviceRepository

class GetHeadTrackingConfigUseCase(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): Boolean {
        val state = deviceRepository.getDeviceState()
        return state.connected // placeholder for now
    }
}

