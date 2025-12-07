package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.AudioRepository
import me.kavishdevar.librepods.domain.repository.DeviceRepository

class UpdateHearingAidAdjustmentsUseCase(
    private val audioRepository: AudioRepository,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(params: Map<String, Float>) {
        // TODO: validate and push to device DSP
    }
}

