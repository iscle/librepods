package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.HearingProfile
import me.kavishdevar.librepods.domain.repository.AudioRepository

class GetHearingProfileUseCase(
    private val audioRepository: AudioRepository
) {
    suspend operator fun invoke(): HearingProfile? = audioRepository.getHearingProfile()
}

