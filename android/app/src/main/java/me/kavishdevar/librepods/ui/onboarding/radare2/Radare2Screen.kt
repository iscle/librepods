package me.kavishdevar.librepods.ui.onboarding.radare2

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.kavishdevar.librepods.ui.component.StyledScaffold
import me.kavishdevar.librepods.ui.component.StyledTopAppBar
import me.kavishdevar.librepods.ui.onboarding.OnboardingViewModel
import me.kavishdevar.librepods.utils.RadareOffsetFinder
import timber.log.Timber

@Composable
fun Radare2Screen(
    onNext: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val accentColor = if (isDarkTheme) Color(0xFF007AFF) else Color(0xFF3C6DF5)

    var hasStarted by remember { mutableStateOf(false) }
    val progressState by viewModel.radareOffsetFinder.progressState.collectAsStateWithLifecycle()
    var moduleEnabled by remember { mutableStateOf(false) }
    var bluetoothToggled by remember { mutableStateOf(false) }
    var isComplete by remember { mutableStateOf(false) }

    LaunchedEffect(hasStarted) {
        if (hasStarted && uiState.isRootCheckPassed) {
            Timber.d("Checking if hook offset is available...")
            val isHookReady = viewModel.radareOffsetFinder.isHookOffsetAvailable()
            Timber.d("Hook offset ready: $isHookReady")

            if (isHookReady) {
                Timber.d("Hook is ready")
                isComplete = true
            } else {
                Timber.d("Hook not ready, starting setup process...")
                withContext(Dispatchers.IO) {
                    viewModel.radareOffsetFinder.setupAndFindOffset()
                }
            }
        }
    }

    LaunchedEffect(progressState) {
        if (progressState is RadareOffsetFinder.ProgressState.Success) {
            isComplete = true
        }
    }

    StyledScaffold(
        topBar = {
            StyledTopAppBar(
                title = {
                    Text("Setting Up")
                }
            )
        },
    ) { innerPadding ->
    Column(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(rememberLayerBackdrop())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            Spacer(Modifier.height(spacerHeight))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StatusIcon(
                        if (hasStarted) progressState else RadareOffsetFinder.ProgressState.Idle,
                        isDarkTheme
                    )

                    Spacer(Modifier.height(24.dp))

                    AnimatedContent(
                        targetState = if (hasStarted) getStatusTitle(
                            progressState,
                            moduleEnabled, bluetoothToggled
                        ) else "Setup Required",
                        transitionSpec = { fadeIn() togetherWith fadeOut() }
                    ) { text ->
                        Text(
                            text = text,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = textColor
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    AnimatedContent(
                        targetState = if (hasStarted)
                            getStatusDescription(progressState, moduleEnabled, bluetoothToggled)
                        else
                            "AirPods functionality requires one-time setup for hooking into Bluetooth library",
                        transitionSpec = { fadeIn() togetherWith fadeOut() }
                    ) { text ->
                        Text(
                            text = text,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    if (!hasStarted) {
                        Button(
                            onClick = { hasStarted = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accentColor
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Start Setup",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        when (progressState) {
                            is RadareOffsetFinder.ProgressState.DownloadProgress -> {
                                val progress =
                                    (progressState as RadareOffsetFinder.ProgressState.DownloadProgress).progress
                                val animatedProgress by animateFloatAsState(
                                    targetValue = progress,
                                    label = "Download Progress"
                                )
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    LinearProgressIndicator(
                                        progress = { animatedProgress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp),
                                        strokeCap = StrokeCap.Round,
                                        color = accentColor
                                    )

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = "${(progress * 100).toInt()}%",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = textColor.copy(alpha = 0.6f)
                                    )
                                }
                            }

                            is RadareOffsetFinder.ProgressState.Success -> {
                                if (!moduleEnabled) {
                                    Button(
                                        onClick = { moduleEnabled = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = accentColor
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            "I've Enabled/Reactivated the Module",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } else if (!bluetoothToggled) {
                                    Button(
                                        onClick = { bluetoothToggled = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = accentColor
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            "I've Toggled Bluetooth",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            onNext()
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = accentColor
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            "Continue to Settings",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            is RadareOffsetFinder.ProgressState.Idle,
                            is RadareOffsetFinder.ProgressState.Error -> {
                                // No specific UI for these states
                            }

                            else -> {
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp),
                                    strokeCap = StrokeCap.Round,
                                    color = accentColor
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            if (progressState is RadareOffsetFinder.ProgressState.Error && !isComplete && hasStarted) {
                Button(
                    onClick = {
                        Timber.d("Trying to find offset again...")
                        MainScope().launch {
                            withContext(Dispatchers.IO) {
                                viewModel.radareOffsetFinder.setupAndFindOffset()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Try Again",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusIcon(
    progressState: RadareOffsetFinder.ProgressState,
    isDarkTheme: Boolean
) {
    val accentColor = if (isDarkTheme) Color(0xFF007AFF) else Color(0xFF3C6DF5)
    val errorColor = if (isDarkTheme) Color(0xFFFF453A) else Color(0xFFFF3B30)
    val successColor = if (isDarkTheme) Color(0xFF30D158) else Color(0xFF34C759)

    Box(
        modifier = Modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        when (progressState) {
            is RadareOffsetFinder.ProgressState.Error -> {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Error",
                    tint = errorColor,
                    modifier = Modifier.size(50.dp)
                )
            }

            is RadareOffsetFinder.ProgressState.Success -> {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = successColor,
                    modifier = Modifier.size(50.dp)
                )
            }

            is RadareOffsetFinder.ProgressState.Idle -> {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = accentColor,
                    modifier = Modifier.size(50.dp)
                )
            }

            else -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = accentColor,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}

private fun getStatusTitle(
    state: RadareOffsetFinder.ProgressState,
    moduleEnabled: Boolean,
    bluetoothToggled: Boolean
): String {
    return when (state) {
        is RadareOffsetFinder.ProgressState.Success -> {
            when {
                !moduleEnabled -> "Enable Xposed Module"
                !bluetoothToggled -> "Toggle Bluetooth"
                else -> "Setup Complete"
            }
        }

        is RadareOffsetFinder.ProgressState.Idle -> "Getting Ready"
        is RadareOffsetFinder.ProgressState.CheckingExisting -> "Checking if radare2 already downloaded"
        is RadareOffsetFinder.ProgressState.Downloading -> "Downloading radare2"
        is RadareOffsetFinder.ProgressState.DownloadProgress -> "Downloading radare2"
        is RadareOffsetFinder.ProgressState.Extracting -> "Extracting radare2"
        is RadareOffsetFinder.ProgressState.MakingExecutable -> "Setting executable permissions"
        is RadareOffsetFinder.ProgressState.FindingOffset -> "Finding function offset"
        is RadareOffsetFinder.ProgressState.SavingOffset -> "Saving offset"
        is RadareOffsetFinder.ProgressState.Cleaning -> "Cleaning Up"
        is RadareOffsetFinder.ProgressState.Error -> "Setup Failed"
    }
}

private fun getStatusDescription(
    state: RadareOffsetFinder.ProgressState,
    moduleEnabled: Boolean,
    bluetoothToggled: Boolean
): String {
    return when (state) {
        is RadareOffsetFinder.ProgressState.Success -> {
            when {
                !moduleEnabled -> "Please enable the LibrePods Xposed module in your Xposed manager (e.g. LSPosed). If already enabled, disable and re-enable it."
                !bluetoothToggled -> "Please turn off and then turn on Bluetooth to apply the changes."
                else -> "All set! You can now use your AirPods with enhanced functionality."
            }
        }

        is RadareOffsetFinder.ProgressState.Idle -> "Preparing"
        is RadareOffsetFinder.ProgressState.CheckingExisting -> "Checking if radare2 are already installed"
        is RadareOffsetFinder.ProgressState.Downloading -> "Starting radare2 download"
        is RadareOffsetFinder.ProgressState.DownloadProgress -> "Downloading radare2"
        is RadareOffsetFinder.ProgressState.Extracting -> "Extracting radare2"
        is RadareOffsetFinder.ProgressState.MakingExecutable -> "Setting executable permissions on radare2 binaries"
        is RadareOffsetFinder.ProgressState.FindingOffset -> "Looking for the required Bluetooth function in system libraries"
        is RadareOffsetFinder.ProgressState.SavingOffset -> "Saving the function offset"
        is RadareOffsetFinder.ProgressState.Cleaning -> "Removing temporary extracted files"
        is RadareOffsetFinder.ProgressState.Error -> state.message
    }
}
