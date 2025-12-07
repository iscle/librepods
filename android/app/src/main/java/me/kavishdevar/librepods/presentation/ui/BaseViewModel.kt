package me.kavishdevar.librepods.presentation.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<T>(
    initialState: T
) : ViewModel() {
    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    fun setState(reducer: T.() -> T) {
        _uiState.value = _uiState.value.reducer()
    }
}
