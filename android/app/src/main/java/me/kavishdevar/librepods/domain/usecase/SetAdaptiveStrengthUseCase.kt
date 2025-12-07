package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.AudioRepository

class SetAdaptiveStrengthUseCase(
    private val audioRepository: AudioRepository
) {
    suspend operator fun invoke(params: Int) {
        // TODO: clamp to capability
        audioRepository.setAdaptiveStrength(params)
    }
}

