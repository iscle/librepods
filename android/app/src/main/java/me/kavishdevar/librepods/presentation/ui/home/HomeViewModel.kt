package me.kavishdevar.librepods.presentation.ui.home

import dagger.hilt.android.lifecycle.HiltViewModel
import me.kavishdevar.librepods.domain.repository.AirPodsRepository
import me.kavishdevar.librepods.presentation.ui.BaseViewModel
import javax.inject.Inject

data class HomeUiState(
    val isConnected: Boolean = true,
    val name: String = "AirPods Pro",
    val model: String = "A1234",
    val serial: String = "ABCDEF01234",
    val version: String = "?",
    val hasHearingHealthCapability: Boolean = true,
    val hasPpeCapability: Boolean = true,
    val hasListeningModeCapability: Boolean = true,
    val hasStemConfigCapability: Boolean = true,
    val hasSleepDetectionCapability: Boolean = true,
    val hasHeadGesturesCapability: Boolean = true,
    val hasLoudSoundReductionCapability: Boolean = true,
    val isHeadGesturesEnabled: Boolean = true,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val airPodsRepository: AirPodsRepository,
) : BaseViewModel<HomeUiState>(HomeUiState()) {

    fun setDonationDialogShown() {
        TODO("Not yet implemented")
    }

    fun reconnectToLastDevice() {
        TODO("Not yet implemented")
    }
}
