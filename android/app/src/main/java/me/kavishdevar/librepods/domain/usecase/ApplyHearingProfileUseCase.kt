package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.HearingProfile
import me.kavishdevar.librepods.domain.repository.AudioRepository
import me.kavishdevar.librepods.domain.repository.DeviceRepository

class ApplyHearingProfileUseCase(
    private val audioRepository: AudioRepository,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(params: HearingProfile) {
        // TODO: translate to DSP params and push to device
        audioRepository.saveHearingProfile(params)
    }
}

