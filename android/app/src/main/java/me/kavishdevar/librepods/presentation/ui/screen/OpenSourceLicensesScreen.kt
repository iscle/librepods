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

package me.kavishdevar.librepods.presentation.ui.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.produceLibraries
import kotlinx.coroutines.Job
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.presentation.ui.component.LiquidButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidIconButton
import me.kavishdevar.librepods.presentation.ui.component.LiquidScaffold
import me.kavishdevar.librepods.presentation.ui.component.LiquidTopAppBar

private var debounceJob: Job? = null

@Composable
fun OpenSourceLicensesScreen(
    onNavigateBack: () -> Unit,
) {
    isSystemInDarkTheme()

    LiquidScaffold(
        topBar = {
            LiquidTopAppBar(
                title = {
                    Text(stringResource(R.string.open_source_licenses))
                },
                navigationIcon = {
                    LiquidIconButton(
                        onClick = onNavigateBack,
                        backdrop = rememberLayerBackdrop(),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Text("\uDBC2\uDFF6")
                    }
                },
                backdrop = backdrop
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .layerBackdrop(backdrop)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            Spacer(Modifier.height(spacerHeight))
            val context = LocalContext.current
            val libraries by produceLibraries {
                context.resources.openRawResource(R.raw.aboutlibraries)
                    .bufferedReader()
                    .use { it.readText() }
            }
            LibrariesContainer(
                libraries = libraries,
                modifier = Modifier
                    .padding(0.dp)
                    .fillMaxSize()
            )
        }
    }
}
