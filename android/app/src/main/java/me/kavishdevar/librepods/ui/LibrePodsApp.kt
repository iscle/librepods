package me.kavishdevar.librepods.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import me.kavishdevar.librepods.screens.AccessibilitySettingsScreen
import me.kavishdevar.librepods.screens.AdaptiveStrengthScreen
import me.kavishdevar.librepods.screens.AppSettingsScreen
import me.kavishdevar.librepods.screens.CameraControlScreen
import me.kavishdevar.librepods.screens.DebugScreen
import me.kavishdevar.librepods.screens.HearingAidAdjustmentsScreen
import me.kavishdevar.librepods.screens.HearingAidScreen
import me.kavishdevar.librepods.screens.HearingProtectionScreen
import me.kavishdevar.librepods.screens.LongPress
import me.kavishdevar.librepods.screens.OpenSourceLicensesScreen
import me.kavishdevar.librepods.screens.RenameScreen
import me.kavishdevar.librepods.screens.TransparencySettingsScreen
import me.kavishdevar.librepods.screens.TroubleshootingScreen
import me.kavishdevar.librepods.screens.UpdateHearingTestScreen
import me.kavishdevar.librepods.ui.headtracking.HeadTrackingScreen
import me.kavishdevar.librepods.ui.home.HomeScreen
import me.kavishdevar.librepods.ui.onboarding.OnboardingScreen
import me.kavishdevar.librepods.ui.splash.SplashScreen
import me.kavishdevar.librepods.ui.version.VersionScreen

sealed class Destination(val route: String) {
    object Splash : Destination("splash")
    object Home : Destination("settings")
    object Onboarding : Destination("onboarding")
    object Debug : Destination("debug")
    object Rename : Destination("rename")
    object AppSettings : Destination("app_settings")
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
                onNavigateToTroubleshooting = {
                    navController.navigate(Destination.Troubleshooting.route)
                }
            )
        }
        composable(Destination.Debug.route) { DebugScreen() }
        composable(Destination.LongPress.route) { navBackStackEntry ->
            LongPress(
                name = navBackStackEntry.arguments?.getString(Destination.LongPress.BudArg)!!
            )
        }
        composable(Destination.Rename.route) { RenameScreen() }
        composable(Destination.AppSettings.route) {
            AppSettingsScreen(
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
        composable(Destination.Troubleshooting.route) { TroubleshootingScreen() }
        composable(Destination.HeadTracking.route) { HeadTrackingScreen() }
        composable(Destination.Accessibility.route) {
            AccessibilitySettingsScreen(
                onNavigateToTransparencyCustomization = { navController.navigate(Destination.TransparencyCustomization.route) }
            )
        }
        composable(Destination.TransparencyCustomization.route) { TransparencySettingsScreen() }
        composable(Destination.HearingAid.route) {
            HearingAidScreen(
                onNavigateToAdjustments = { navController.navigate(Destination.HearingAidAdjustments.route) },
                onNavigateToUpdateHearingTest = { navController.navigate(Destination.UpdateHearingTest.route) }
            )
        }
        composable(Destination.HearingAidAdjustments.route) { HearingAidAdjustmentsScreen() }
        composable(Destination.AdaptiveStrength.route) { AdaptiveStrengthScreen() }
        composable(Destination.CameraControl.route) { CameraControlScreen() }
        composable(Destination.OpenSourceLicenses.route) { OpenSourceLicensesScreen() }
        composable(Destination.UpdateHearingTest.route) { UpdateHearingTestScreen() }
        composable(Destination.VersionInfo.route) { VersionScreen() }
        composable(Destination.HearingProtection.route) { HearingProtectionScreen() }
    }
}
