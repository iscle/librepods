package me.kavishdevar.librepods.ui.headtracking

import dagger.hilt.android.lifecycle.HiltViewModel
import me.kavishdevar.librepods.domain.repository.AirPodsRepository
import me.kavishdevar.librepods.ui.BaseViewModel
import javax.inject.Inject

data class HeadTrackingUiState(
    val isHeadTrackingActive: Boolean = false,
)

@HiltViewModel
class HeadTrackingViewModel @Inject constructor(
    private val airPodsRepository: AirPodsRepository,
) : BaseViewModel<HeadTrackingUiState>(HeadTrackingUiState()) {
    fun startHeadTracking() {
        TODO("Not yet implemented")
    }

    fun stopHeadTracking() {
        TODO("Not yet implemented")
    }
    // TODO
}
