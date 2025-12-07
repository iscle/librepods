package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.HearingProfile
import me.kavishdevar.librepods.domain.repository.AudioRepository

class UpdateHearingTestResultsUseCase(
    private val audioRepository: AudioRepository
) { // Placeholder using HearingProfile as input
    suspend operator fun invoke(params: HearingProfile) {
        // TODO: validate audiogram and regenerate profile
        audioRepository.saveHearingProfile(params)
    }
}

