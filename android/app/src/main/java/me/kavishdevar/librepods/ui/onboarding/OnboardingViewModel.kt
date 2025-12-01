package me.kavishdevar.librepods.ui.onboarding

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.kavishdevar.librepods.ui.BaseViewModel
import me.kavishdevar.librepods.utils.RadareOffsetFinder
import timber.log.Timber
import javax.inject.Inject

sealed class OnboardingStep(
    val route: String,
) {
    data object Permissions : OnboardingStep("permissions")
    data object Root : OnboardingStep("root")
    data object Radare2 : OnboardingStep("radare2")
    data object Finish : OnboardingStep("finish")
}

data class OnboardingUiState(
    val step: OnboardingStep = OnboardingStep.Permissions,
    val isCheckingRoot: Boolean = false,
    val isRootCheckPassed: Boolean = false,
    val isRootCheckFailed: Boolean = false,
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<OnboardingUiState>(OnboardingUiState()) {
    val radareOffsetFinder = RadareOffsetFinder(context)

    fun checkRoot() {
        setState {
            copy(
                isCheckingRoot = true,
                isRootCheckFailed = false
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val exitValue = Runtime.getRuntime().exec("su -c id").waitFor()
                setState {
                    copy(
                        isCheckingRoot = false,
                        isRootCheckPassed = (exitValue == 0),
                        isRootCheckFailed = (exitValue != 0)
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Root check failed")
                setState {
                    copy(
                        isCheckingRoot = false,
                        isRootCheckPassed = false,
                        isRootCheckFailed = true
                    )
                }
            }
        }
    }

    fun nextStep() {
        setState {
            val nextStep = when (step) {
                OnboardingStep.Permissions -> OnboardingStep.Root
                OnboardingStep.Root -> OnboardingStep.Radare2
                OnboardingStep.Radare2 -> OnboardingStep.Finish
                OnboardingStep.Finish -> OnboardingStep.Finish
            }
            copy(step = nextStep)
        }
    }
}
