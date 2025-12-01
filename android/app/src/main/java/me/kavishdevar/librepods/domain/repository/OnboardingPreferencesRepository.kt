package me.kavishdevar.librepods.domain.repository

import kotlinx.coroutines.flow.Flow

interface OnboardingPreferencesRepository {
    val onboardingShown: Flow<Boolean>
    suspend fun setOnboardingShown(shown: Boolean)
}
