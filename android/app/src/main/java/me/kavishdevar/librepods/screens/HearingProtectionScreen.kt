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

package me.kavishdevar.librepods.screens

import androidx.compose.runtime.Composable
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import me.kavishdevar.librepods.services.ServiceManager

@Composable
fun HearingProtectionScreen() {
    val service = ServiceManager.getService()
    if (service == null) return

    val backdrop = rememberLayerBackdrop()

//    StyledScaffold(
//        title = stringResource(R.string.hearing_protection),
//    ) { spacerHeight ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .layerBackdrop(backdrop)
//                .padding(horizontal = 16.dp)
//        ) {
//            Spacer(Modifier.height(spacerHeight))
//
////            StyledToggle(
////                title = stringResource(R.string.environmental_noise),
////                label = stringResource(R.string.loud_sound_reduction),
////                description = stringResource(R.string.loud_sound_reduction_description),
////                attHandle = ATTHandles.LOUD_SOUND_REDUCTION
////            )
//
//            Spacer(Modifier.height(12.dp))
////            StyledToggle(
////                title = stringResource(R.string.workspace_use),
////                label = stringResource(R.string.ppe),
////                description = stringResource(R.string.workspace_use_description),
////                controlCommandIdentifier = AACPManager.Companion.ControlCommandIdentifiers.PPE_TOGGLE_CONFIG
////            )
//        }
//    }
}
