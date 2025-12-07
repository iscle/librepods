package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.DeviceState
import me.kavishdevar.librepods.domain.repository.DeviceRepository

class RunDeviceHealthCheckUseCase(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): DeviceState = deviceRepository.getDeviceState()
}

