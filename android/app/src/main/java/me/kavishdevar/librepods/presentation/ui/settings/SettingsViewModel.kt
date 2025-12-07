package me.kavishdevar.librepods.presentation.ui.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import me.kavishdevar.librepods.domain.repository.AirPodsRepository
import me.kavishdevar.librepods.presentation.ui.BaseViewModel
import javax.inject.Inject

data class SettingsUiState(
    val example: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val airPodsRepository: AirPodsRepository,
) : BaseViewModel<SettingsUiState>(SettingsUiState())
