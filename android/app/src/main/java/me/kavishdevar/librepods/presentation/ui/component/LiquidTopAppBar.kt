package me.kavishdevar.librepods.presentation.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.Backdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme

val StyledTopBarHeight = 54.dp
val StyledTopBarHorizontalPadding = 12.dp

interface LiquidTopAppBarScope {
    val backdrop: Backdrop
}

private data class LiquidTopAppBarScopeImpl(
    override val backdrop: Backdrop,
) : LiquidTopAppBarScope

@Composable
fun LiquidTopAppBar(
    title: @Composable LiquidTopAppBarScope.() -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable LiquidTopAppBarScope.() -> Unit = {},
    actions: @Composable LiquidTopAppBarScope.() -> Unit = {},
    expandedHeight: Dp = StyledTopBarHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    backdrop: Backdrop
) {
    val darkTheme = isSystemInDarkTheme()
    val topAppBarBackdrop = rememberLayerBackdrop()

    val state = LiquidTopAppBarScopeImpl(
        backdrop = topAppBarBackdrop,
    )

    Box(
        modifier = modifier
            .drawBackdrop(
                backdrop = backdrop,
                shape = { RectangleShape },
                effects = {
                    blur(24.dp.toPx())
                },
                highlight = null,
                shadow = null,
                onDrawSurface = {
                    drawRect(if (darkTheme) Color.Black.copy(0.75f) else Color.White.copy(0.85f))
                },
                exportedBackdrop = topAppBarBackdrop
            )
            .windowInsetsPadding(windowInsets)
    ) {
        Box(
            modifier = modifier
                .height(expandedHeight)
                .fillMaxWidth()
        ) {
            // Title
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = StyledTopBarHorizontalPadding),
            ) {
                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.titleLarge) {
                    state.title()
                }
            }

            // Navigation icon
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = StyledTopBarHorizontalPadding)
            ) {
                state.navigationIcon()
            }

            // Actions
            Row(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = StyledTopBarHorizontalPadding)
            ) {
                state.actions()
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun LiquidTopAppBarPreview() {
    LibrePodsTheme {
        Surface {
            LiquidTopAppBar(
                title = {
                    Text("Title")
                },
                navigationIcon = {
                    LiquidButton(
                        onClick = {},
                        backdrop = rememberLayerBackdrop(),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Text("\uDBC2\uDFF6")
                    }
                },
                backdrop = rememberLayerBackdrop()
            )
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LiquidTopAppBarDarkPreview() {
    LibrePodsTheme {
        Surface {
            LiquidTopAppBar(
                title = {
                    Text("Title")
                },
                navigationIcon = {
                    LiquidIconButton(
                        onClick = {},
                        backdrop = rememberLayerBackdrop(),
                    ) {
                        Text("\uDBC2\uDFF6")
                    }
                },
                backdrop = rememberLayerBackdrop()
            )
        }
    }
}
