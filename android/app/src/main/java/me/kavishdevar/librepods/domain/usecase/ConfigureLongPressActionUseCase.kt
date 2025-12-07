package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.BudSide
import me.kavishdevar.librepods.domain.repository.DeviceRepository
import me.kavishdevar.librepods.domain.repository.FirmwareRepository

class ConfigureLongPressActionUseCase(
    private val deviceRepository: DeviceRepository,
    private val firmwareRepository: FirmwareRepository
) {
    data class Params(val side: BudSide, val action: String)
    suspend operator fun invoke(params: Params): Boolean {
        // TODO: validate and capability check
        val supported = deviceRepository.supportsLongPress()
        if (!supported) return false
        return deviceRepository.setLongPressAction(params.side, params.action)
    }
}

