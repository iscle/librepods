/*
    LibrePods - AirPods liberated from Apple’s ecosystem
    Copyright (C) 2025 LibrePods contributors

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package me.kavishdevar.librepods.presentation.ui.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.kavishdevar.librepods.presentation.ui.onboarding.permissions.PermissionsScreen
import me.kavishdevar.librepods.presentation.ui.onboarding.radare2.Radare2Screen
import me.kavishdevar.librepods.presentation.ui.onboarding.root.RootScreen
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme
import timber.log.Timber

@Composable
fun OnboardingScreen(
    onNavigateToSettings: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = OnboardingStep.Permissions.route,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable(OnboardingStep.Permissions.route) {
            PermissionsScreen(
                onNext = {
                    viewModel.nextStep()
                }
            )
        }
        composable(OnboardingStep.Root.route) {
            RootScreen(
                onNext = {
                    viewModel.nextStep()
                }
            )
        }
        composable(OnboardingStep.Radare2.route) {
            Radare2Screen(
                onNext = {
                    viewModel.nextStep()
                }
            )
        }
        composable(OnboardingStep.Finish.route) {
            LaunchedEffect(Unit) {
                Timber.i("Onboarding finished, navigating to settings")
                onNavigateToSettings()
            }
        }
    }

    LaunchedEffect(uiState.step) {
        if (navController.currentDestination?.route != uiState.step.route) {
            navController.navigate(uiState.step.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }
}

@Preview
@Composable
fun OnboardingScreenPreview() {
    LibrePodsTheme {
        OnboardingScreen(
            onNavigateToSettings = {},
        )
    }
}

