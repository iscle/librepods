package me.kavishdevar.librepods.presentation.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.kavishdevar.librepods.presentation.ui.accessibility.AccessibilitySettingsScreen
import me.kavishdevar.librepods.presentation.ui.debug.DebugScreen
import me.kavishdevar.librepods.presentation.ui.headtracking.HeadTrackingScreen
import me.kavishdevar.librepods.presentation.ui.home.HomeScreen
import me.kavishdevar.librepods.presentation.ui.onboarding.OnboardingScreen
import me.kavishdevar.librepods.presentation.ui.screen.AdaptiveStrengthScreen
import me.kavishdevar.librepods.presentation.ui.screen.CameraControlScreen
import me.kavishdevar.librepods.presentation.ui.screen.HearingAidAdjustmentsScreen
import me.kavishdevar.librepods.presentation.ui.screen.HearingAidScreen
import me.kavishdevar.librepods.presentation.ui.screen.HearingProtectionScreen
import me.kavishdevar.librepods.presentation.ui.screen.LongPress
import me.kavishdevar.librepods.presentation.ui.screen.OpenSourceLicensesScreen
import me.kavishdevar.librepods.presentation.ui.screen.TransparencySettingsScreen
import me.kavishdevar.librepods.presentation.ui.screen.TroubleshootingScreen
import me.kavishdevar.librepods.presentation.ui.screen.UpdateHearingTestScreen
import me.kavishdevar.librepods.presentation.ui.settings.SettingsScreen
import me.kavishdevar.librepods.presentation.ui.splash.SplashScreen
import me.kavishdevar.librepods.presentation.ui.version.VersionScreen

sealed class Destination(val route: String) {
    object Splash : Destination("splash")
    object Onboarding : Destination("onboarding")
    object Home : Destination("home")
    object Debug : Destination("debug")
    object Settings : Destination("settings")
    object Troubleshooting : Destination("troubleshooting")
    object HeadTracking : Destination("head_tracking")
    object Accessibility : Destination("accessibility")
    object TransparencyCustomization : Destination("transparency_customization")
    object HearingAid : Destination("hearing_aid")
    object HearingAidAdjustments : Destination("hearing_aid_adjustments")
    object AdaptiveStrength : Destination("adaptive_strength")
    object CameraControl : Destination("camera_control")
    object OpenSourceLicenses : Destination("open_source_licenses")
    object UpdateHearingTest : Destination("update_hearing_test")
    object VersionInfo : Destination("version_info")
    object HearingProtection : Destination("hearing_protection")
    object LongPress : Destination("long_press/{bud}") {
        const val BudArg: String = "bud"
        fun route(bud: String) = "long_press/$bud"

        val LeftRoute = route("left")
        val RightRoute = route("right")
    }
}

@Composable
fun LibrePodsApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Home.route,
        modifier = Modifier.fillMaxSize(),
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(durationMillis = 300)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it / 4 },
                animationSpec = tween(durationMillis = 300)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it / 4 },
                animationSpec = tween(durationMillis = 300)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(durationMillis = 300)
            )
        }
    ) {
        composable(Destination.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Destination.Onboarding.route) {
                        popUpTo(Destination.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Splash.route) { inclusive = true }
                    }
                },
            )
        }
        composable(Destination.Onboarding.route) {
            OnboardingScreen(
                onNavigateToSettings = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Destination.Home.route) {
            HomeScreen(
                onNavigateToSettings = {
                    navController.navigate(Destination.Settings.route)
                },
                onNavigateToHearingProtection = {
                    navController.navigate(Destination.HearingProtection.route)
                },
                onNavigateToHearingAid = {
                    navController.navigate(Destination.HearingAid.route)
                },
                onNavigateToLeft = {
                    navController.navigate(Destination.LongPress.LeftRoute)
                },
                onNavigateToRight = {
                    navController.navigate(Destination.LongPress.RightRoute)
                },
                onNavigateToCameraRemote = {
                    navController.navigate(Destination.CameraControl.route)
                },
                onNavigateToAdaptiveAudio = {
                    navController.navigate(Destination.AdaptiveStrength.route)
                },
                onNavigateToHeadGestures = {
                    navController.navigate(Destination.HeadTracking.route)
                },
                onNavigateToAccessibility = {
                    navController.navigate(Destination.Accessibility.route)
                },
                onNavigateToVersion = {
                    navController.navigate(Destination.VersionInfo.route)
                },
                onNavigateToDebug = {
                    navController.navigate(Destination.Debug.route)
                },
                onNavigateToTroubleshooting = {
                    navController.navigate(Destination.Troubleshooting.route)
                }
            )
        }
        composable(Destination.Debug.route) {
            DebugScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Destination.LongPress.route) { navBackStackEntry ->
            LongPress(
                onNavigateBack = { navController.popBackStack() },
                name = navBackStackEntry.arguments?.getString(Destination.LongPress.BudArg)!!
            )
        }
        composable(Destination.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTroubleshooting = { navController.navigate(Destination.Troubleshooting.route) },
                onNavigateToOpenSourceLicenses = { navController.navigate(Destination.OpenSourceLicenses.route) },
                onNavigateToOnboarding = {
                    navController.navigate(Destination.Onboarding.route) {
                        popUpTo(Destination.Home.route) { inclusive = true }
                    }
                },
                onShowCameraPackageDialog = { /* show dialog handled internally via state in screen */ }
            )
        }
        composable(Destination.Troubleshooting.route) {
            TroubleshootingScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Destination.HeadTracking.route) {
            HeadTrackingScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Destination.Accessibility.route) {
            AccessibilitySettingsScreen(
                onNavigateToTransparencyCustomization = { navController.navigate(Destination.TransparencyCustomization.route) }
            )
        }
        composable(Destination.TransparencyCustomization.route) {
            TransparencySettingsScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Destination.HearingAid.route) {
            HearingAidScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAdjustments = { navController.navigate(Destination.HearingAidAdjustments.route) },
                onNavigateToUpdateHearingTest = { navController.navigate(Destination.UpdateHearingTest.route) }
            )
        }
        composable(Destination.HearingAidAdjustments.route) {
            HearingAidAdjustmentsScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Destination.AdaptiveStrength.route) {
            AdaptiveStrengthScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Destination.CameraControl.route) {
            CameraControlScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Destination.OpenSourceLicenses.route) {
            OpenSourceLicensesScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Destination.UpdateHearingTest.route) {
            UpdateHearingTestScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Destination.VersionInfo.route) {
            VersionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Destination.HearingProtection.route) {
            HearingProtectionScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
