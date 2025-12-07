package me.kavishdevar.librepods.domain.repository

import me.kavishdevar.librepods.domain.model.HearingProfile

interface AudioRepository {
    suspend fun getHearingProfile(): HearingProfile?
    suspend fun saveHearingProfile(profile: HearingProfile)
    suspend fun getAdaptiveStrength(): Int
    suspend fun setAdaptiveStrength(value: Int)
}

