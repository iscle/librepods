package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.AudioRepository

class GetHearingAidAdjustmentsUseCase(
    private val audioRepository: AudioRepository
) { // Placeholder structure
    suspend operator fun invoke(): Map<String, Float> = emptyMap()
}

