package me.kavishdevar.librepods.data.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

object OnboardingPreferences {
    val OnboardingShown = booleanPreferencesKey("onboarding_shown")
}
