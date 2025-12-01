package me.kavishdevar.librepods.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.kavishdevar.librepods.ui.theme.LibrePodsTheme

val StyledTopBarHeight = 54.dp
val StyledTopBarHorizontalPadding = 12.dp

@Composable fun styledTopAppBarColors() = TopAppBarDefaults.topAppBarColors().copy(
    containerColor = Color.Transparent
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    expandedHeight: Dp = StyledTopBarHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    colors: TopAppBarColors = styledTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                modifier = Modifier.padding(horizontal = StyledTopBarHorizontalPadding)
            ) {
                title()
            }
        },
        modifier = modifier,
        navigationIcon = {
            Box(
                modifier = Modifier.padding(start = StyledTopBarHorizontalPadding)
            ) {
                navigationIcon()
            }
        },
        actions = {
            Row(
                modifier = Modifier.padding(end = StyledTopBarHorizontalPadding)
            ) {
                actions()
            }
        },
        expandedHeight = expandedHeight,
        windowInsets = windowInsets,
        colors = colors,
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun StyledTopAppBarPreview() {
    LibrePodsTheme {
        Surface {
            StyledTopAppBar(
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
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun StyledTopAppBarDarkPreview() {
    LibrePodsTheme {
        Surface {
            StyledTopAppBar(
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
                }
            )
        }
    }
}
