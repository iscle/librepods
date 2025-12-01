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

package me.kavishdevar.librepods.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.rememberHazeState
import me.kavishdevar.librepods.ui.theme.LibrePodsTheme

interface StyledScaffoldScope {
    val backdrop: LayerBackdrop
    val hazeState: HazeState
}

private data class StyledScaffoldScopeImpl(
    override val backdrop: LayerBackdrop,
    override val hazeState: HazeState
) : StyledScaffoldScope

@Composable
fun StyledScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable StyledScaffoldScope.() -> Unit = {},
    bottomBar: @Composable StyledScaffoldScope.() -> Unit = {},
    snackbarHost: @Composable StyledScaffoldScope.() -> Unit = {},
    content: @Composable StyledScaffoldScope.(PaddingValues) -> Unit
) {
    val backdrop = rememberLayerBackdrop()
    val hazeState = rememberHazeState()

    val scope = StyledScaffoldScopeImpl(
        backdrop = backdrop,
        hazeState = hazeState
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun StyledScaffoldPreviewApp() {
    LibrePodsTheme {
        StyledScaffold(
            topBar = {
                StyledTopAppBar(
                    title = {
                        Text("Example title")
                    }
                )
            },
        ) { innerPadding ->
            Text("Example content", modifier = Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun StyledScaffoldDarkPreviewApp() {
    LibrePodsTheme {
        StyledScaffold(
            topBar = {
                StyledTopAppBar(
                    title = {
                        Text("Example title")
                    }
                )
            },
        ) { innerPadding ->
            Text("Example content", modifier = Modifier.padding(innerPadding))
        }
    }
}
