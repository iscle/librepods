package me.kavishdevar.librepods.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.kavishdevar.librepods.domain.repository.OnboardingPreferencesRepository

private val Context.dataStore by preferencesDataStore(name = "onboarding")

class OnboardingPreferencesRepositoryImpl(
    private val context: Context,
) : OnboardingPreferencesRepository {
    override val onboardingShown: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[OnboardingPreferences.OnboardingShown] ?: false
        }

    override suspend fun setOnboardingShown(shown: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[OnboardingPreferences.OnboardingShown] = shown
        }
    }
}
