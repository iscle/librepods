package me.kavishdevar.librepods.ui.onboarding.permissions

import android.Manifest
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.ui.onboarding.OnboardingViewModel

private const val TAG = "PermissionsScreen"

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(
    onNext: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) Color(0xFF1C1C1E) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val accentColor = if (isDarkTheme) Color(0xFF007AFF) else Color(0xFF3C6DF5)

    val bluetoothPermissions = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_ADVERTISE,

            )
        } else {
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    val otherPermissions = remember {
        listOf(
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ANSWER_PHONE_CALLS
        )
    }

    val allPermissions = bluetoothPermissions + otherPermissions

    val permissionsState = rememberMultiplePermissionsState(
        permissions = allPermissions
    )

    val isAllPermissionsGranted = permissionsState.permissions.all { it.status.isGranted }

    var canDrawOverlays by remember {
        mutableStateOf(Settings.canDrawOverlays(context))
    }

    val manageOverlayPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        canDrawOverlays = Settings.canDrawOverlays(context)
    }

    val transition = rememberInfiniteTransition()
    val pulseScale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(isAllPermissionsGranted, canDrawOverlays) {
        if (isAllPermissionsGranted && canDrawOverlays) {
            onNext()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkTheme) Color.Black else Color(0xFFF2F2F7))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\uDBC2\uDEB7",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center
            )

            Canvas(
                modifier = Modifier
                    .size(120.dp)
                    .scale(pulseScale)
            ) {
                val radius = size.minDimension / 2.2f
                val centerX = size.width / 2
                val centerY = size.height / 2

                drawCircle(
                    color = accentColor.copy(alpha = 0.1f),
                    radius = radius * 1.3f,
                    center = Offset(centerX, centerY)
                )

                drawCircle(
                    color = accentColor.copy(alpha = 0.2f),
                    radius = radius * 1.1f,
                    center = Offset(centerX, centerY)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Permission Required",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.permissions_required),
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = textColor.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(32.dp))

        PermissionCard(
            title = "Bluetooth Permissions",
            description = "Required to communicate with your AirPods",
            icon = ImageVector.vectorResource(id = R.drawable.ic_bluetooth),
            isGranted = permissionsState.permissions.filter {
                it.permission in bluetoothPermissions
            }.all { it.status.isGranted },
            backgroundColor = backgroundColor,
            textColor = textColor,
            accentColor = accentColor
        )

        PermissionCard(
            title = "Notification Permission",
            description = "To show battery status",
            icon = Icons.Default.Notifications,
            isGranted = permissionsState.permissions.find {
                it.permission == "android.permission.POST_NOTIFICATIONS"
            }?.status?.isGranted == true,
            backgroundColor = backgroundColor,
            textColor = textColor,
            accentColor = accentColor
        )

        PermissionCard(
            title = "Phone Permissions",
            description = "For answering calls with Head Gestures",
            icon = Icons.Default.Phone,
            isGranted = permissionsState.permissions.filter {
                it.permission.contains("PHONE") || it.permission.contains("CALLS")
            }.all { it.status.isGranted },
            backgroundColor = backgroundColor,
            textColor = textColor,
            accentColor = accentColor
        )

        PermissionCard(
            title = "Display Over Other Apps",
            description = "For popup animations when AirPods connect",
            icon = ImageVector.vectorResource(id = R.drawable.ic_layers),
            isGranted = canDrawOverlays,
            backgroundColor = backgroundColor,
            textColor = textColor,
            accentColor = accentColor
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { permissionsState.launchMultiplePermissionRequest() },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = accentColor
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Ask for regular permissions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    "package:${context.packageName}".toUri()
                )

                manageOverlayPermissionLauncher.launch(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (canDrawOverlays) Color.Gray else accentColor
            ),
            enabled = !canDrawOverlays,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                if (canDrawOverlays) "Overlay Permission Granted" else "Grant Overlay Permission",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        if (!canDrawOverlays && isAllPermissionsGranted) {
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    onNext()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF757575)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Continue without overlay",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun PermissionCard(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    backgroundColor: Color,
    textColor: Color,
    accentColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isGranted) accentColor.copy(alpha = 0.15f) else Color.Gray.copy(
                            alpha = 0.15f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isGranted) accentColor else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = textColor.copy(alpha = 0.6f)
                )
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isGranted) Color(0xFF4CAF50) else Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isGranted) "✓" else "!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
