package me.kavishdevar.librepods.ui.splash

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import me.kavishdevar.librepods.domain.repository.OnboardingPreferencesRepository
import me.kavishdevar.librepods.ui.BaseViewModel
import javax.inject.Inject

sealed interface SplashEvent {
    data object NavigateToOnboarding : SplashEvent
    data object NavigateToMain : SplashEvent
}

data class SplashUiState(
    val isLoading: Boolean = true,
)

class SplashViewModel(
    private val onboardingPreferencesRepository: OnboardingPreferencesRepository
) : BaseViewModel<SplashUiState>(SplashUiState()) {
    private val _events = MutableSharedFlow<SplashEvent>()
    val events = _events.asSharedFlow()

    init {
        observeOnboarding()
    }

    private fun observeOnboarding() {
        viewModelScope.launch {
            onboardingPreferencesRepository.onboardingShown.collect { shown ->
                setState {
                    copy(
                        isLoading = false,
                    )
                }

                if (shown) {
                    _events.emit(SplashEvent.NavigateToMain)
                } else {
                    _events.emit(SplashEvent.NavigateToOnboarding)
                }
            }
        }
    }
}
