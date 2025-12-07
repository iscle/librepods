package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.AudioRepository

class GetAdaptiveStrengthUseCase(
    private val audioRepository: AudioRepository
) {
    suspend operator fun invoke(): Int = audioRepository.getAdaptiveStrength()
}

