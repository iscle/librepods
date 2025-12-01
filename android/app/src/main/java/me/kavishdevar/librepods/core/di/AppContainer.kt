package me.kavishdevar.librepods.core.di

import android.content.Context
import me.kavishdevar.librepods.data.datastore.OnboardingPreferencesRepositoryImpl
import me.kavishdevar.librepods.domain.repository.OnboardingPreferencesRepository

class AppContainer(context: Context) {
    private val onboardingPreferencesRepositoryImpl by lazy {
        OnboardingPreferencesRepositoryImpl(context)
    }

    val onboardingPreferencesRepository: OnboardingPreferencesRepository
        get() = onboardingPreferencesRepositoryImpl
}
