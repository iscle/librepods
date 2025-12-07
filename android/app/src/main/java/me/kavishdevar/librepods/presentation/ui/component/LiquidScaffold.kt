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

package me.kavishdevar.librepods.presentation.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.kavishdevar.librepods.presentation.ui.theme.LibrePodsTheme

interface LiquidScaffoldScope {
    val backdrop: LayerBackdrop
}

private data class LiquidScaffoldScopeImpl(
    override val backdrop: LayerBackdrop,
) : LiquidScaffoldScope

@Composable
fun LiquidScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable LiquidScaffoldScope.() -> Unit = {},
    bottomBar: @Composable LiquidScaffoldScope.() -> Unit = {},
    snackbarHost: @Composable LiquidScaffoldScope.() -> Unit = {},
    content: @Composable LiquidScaffoldScope.(PaddingValues) -> Unit
) {
    val backdrop = rememberLayerBackdrop()

    val scope = LiquidScaffoldScopeImpl(
        backdrop = backdrop,
    )

    Scaffold(
        modifier = modifier,
        topBar = { scope.topBar() },
        bottomBar = { scope.bottomBar() },
        snackbarHost = { scope.snackbarHost() },
    ) { innerPadding ->
        scope.content(innerPadding)
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun LiquidScaffoldPreviewApp() {
    LibrePodsTheme {
        LiquidScaffold(
            topBar = {
                LiquidTopAppBar(
                    title = {
                        Text("Example title")
                    },
                    backdrop = backdrop
                )
            },
        ) { innerPadding ->
            Text("Example content", modifier = Modifier.padding(innerPadding))
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LiquidScaffoldDarkPreviewApp() {
    LibrePodsTheme {
        LiquidScaffold(
            topBar = {
                LiquidTopAppBar(
                    title = {
                        Text("Example title")
                    },
                    backdrop = backdrop
                )
            },
        ) { innerPadding ->
            Text("Example content", modifier = Modifier.padding(innerPadding))
        }
    }
}
