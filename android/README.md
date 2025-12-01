# LibrePods Android

## DataStore Integration (Onboarding Flag)
This project uses Jetpack DataStore (Preferences) to persist whether the onboarding flow has been shown.

### Key Components
- `DataStoreModule.kt`: Provides `Context.onboardingDataStore` via `preferencesDataStore("onboarding")`.
- `OnboardingPreferences.kt`: Defines the `OnboardingShown` preference key.
- `OnboardingDataStore.kt`: Exposes `onboardingShown: Flow<Boolean>` and `setOnboardingShown(shown: Boolean)`.

### Usage Example (Compose)
```kotlin
val context = LocalContext.current
val onboardingDataStore = remember { OnboardingDataStore.get(context) }
val shown by onboardingDataStore.onboardingShown.collectAsState(initial = false)

LaunchedEffect(Unit) {
    // Mark onboarding as shown after user completes it
    if (!shown) {
        onboardingDataStore.setOnboardingShown(true)
    }
}
```

### Edge Cases & Behavior
- Default value is `false` if the key does not exist or on first app launch.
- IO exceptions when reading produce empty preferences (graceful fallback) and default `false`.
- All writes are atomic via DataStore.

### Future Enhancements
- Add migration from SharedPreferences if legacy storage existed.
- Expand to a Proto DataStore if more structured onboarding data is required.
- Add instrumentation tests for persistence across process death.

### Testing
A placeholder test file `OnboardingDataStoreTest.kt` exists. Add AndroidX Test dependencies to implement context-based tests.


