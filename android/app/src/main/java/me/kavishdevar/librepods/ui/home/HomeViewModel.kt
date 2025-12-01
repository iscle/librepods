package me.kavishdevar.librepods.ui.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.kavishdevar.librepods.domain.model.AirPodsInfo
import me.kavishdevar.librepods.domain.repository.AirPodsRepository
import me.kavishdevar.librepods.ui.BaseViewModel
import javax.inject.Inject

data class HomeUiState(
    val info: AirPodsInfo = AirPodsInfo(),
    val hasHearingHealthCapability: Boolean = false,
    val hasListeningModeCapability: Boolean = false,
    val hasStemConfigCapability: Boolean = false,
    val hasSleepDetectionCapability: Boolean = false,
    val hasHeadGesturesCapability: Boolean = false,
    val hasLoudSoundReductionCapability: Boolean = false,
    val isHeadGesturesEnabled: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val airPodsRepository: AirPodsRepository,
) : BaseViewModel<HomeUiState>(HomeUiState()) {

    init {
        collectAirPodsInfo()
    }

    private fun collectAirPodsInfo() {
        viewModelScope.launch {
            airPodsRepository.airPodsInfo.collect { info ->
                setState {
                    copy(info = info)
                }
            }
        }
    }

    fun setDonationDialogShown() {
        TODO("Not yet implemented")
    }

    fun reconnectToLastDevice() {
        TODO("Not yet implemented")
    }
}
