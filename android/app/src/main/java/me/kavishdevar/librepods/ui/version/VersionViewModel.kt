package me.kavishdevar.librepods.ui.version

import dagger.hilt.android.lifecycle.HiltViewModel
import me.kavishdevar.librepods.domain.repository.AirPodsRepository
import me.kavishdevar.librepods.ui.BaseViewModel
import javax.inject.Inject

data class VersionUiState(
    val version1: String? = null,
    val version2: String? = null,
    val version3: String? = null,
)

@HiltViewModel
class VersionViewModel @Inject constructor(
    private val airPodsRepository: AirPodsRepository,
) : BaseViewModel<VersionUiState>(VersionUiState()) {
    // TODO
}
