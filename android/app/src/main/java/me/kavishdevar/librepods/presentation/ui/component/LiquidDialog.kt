package me.kavishdevar.librepods.presentation.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import com.kyant.backdrop.effects.colorControls
import com.kyant.backdrop.effects.lens
import com.kyant.backdrop.highlight.Highlight
import com.kyant.capsule.ContinuousRoundedRectangle
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme

internal const val LoremIpsum =
    """Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."""

@Composable
fun LiquidDialog(
    onDismissRequest: () -> Unit,
    properties: DialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    ),
    backdrop: Backdrop,
    content: @Composable () -> Unit
) {
    val isLightTheme = !isSystemInDarkTheme()
    if (isLightTheme) Color.Black else Color.White
    if (isLightTheme) Color(0xFF0088FF)
    else Color(0xFF0091FF)
    val containerColor =
        if (isLightTheme) Color(0xFFFAFAFA).copy(0.6f)
        else Color(0xFF121212).copy(0.4f)
    if (isLightTheme) Color(0xFF29293A).copy(0.23f)
    else Color(0xFF121212).copy(0.56f)

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Box(
            Modifier
                .padding(40.dp)
                .drawBackdrop(
                    backdrop = backdrop,
                    shape = { ContinuousRoundedRectangle(48.dp) },
                    effects = {
                        colorControls(
                            brightness = if (isLightTheme) 0.2f else 0f,
                            saturation = 1.5f
                        )
                        blur(if (isLightTheme) 16.dp.toPx() else 8.dp.toPx())
                        lens(24.dp.toPx(), 48.dp.toPx(), depthEffect = true)
                    },
                    highlight = { Highlight.Plain },
                    onDrawSurface = { drawRect(containerColor) }
                )
                .fillMaxWidth()
        ) {
            content()
        }
    }

//    LiquidScaffold(
//        modifier = Modifier.drawWithContent {
//            drawContent()
//            drawRect(dimColor)
//        }
//    ) {

//    }

//    BackdropDemoScaffold(
//        Modifier.drawWithContent {
//            drawContent()
//            drawRect(dimColor)
//        },
//        initialPainterResId = R.drawable.system_home_screen_light
//    ) { backdrop ->
//
//    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun LiquidDialogLightPreview() {
    LibrePodsTheme {
        LiquidDialog(
            onDismissRequest = {},
            backdrop = rememberLayerBackdrop()
        ) {

        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LiquidDialogDarkPreview() {
    LibrePodsTheme {
        LiquidDialog(
            onDismissRequest = {},
            backdrop = rememberLayerBackdrop()
        ) {

        }
    }
}
