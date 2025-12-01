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

package me.kavishdevar.librepods.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.constants.Battery
import me.kavishdevar.librepods.domain.model.AirPodsModel
import me.kavishdevar.librepods.domain.model.BatteryInfo
import me.kavishdevar.librepods.ui.theme.LibrePodsTheme
import me.kavishdevar.librepods.ui.utils.AirPodsResourceMapper

@Composable
fun BatteryView(
    model: AirPodsModel
) {
//    val resources = remember(model) {
//        AirPodsResourceMapper.getResources(model)
//    }
//    var batteryInfo by remember { mutableStateOf(BatteryInfo()) }
//    var previousBatteryInfo by remember { mutableStateOf(BatteryInfo()) }
//    var batteryStatus by remember { mutableStateOf<List<Battery>>(listOf()) }
//    val leftLevel = batteryInfo.left?.level ?: 0
//    val rightLevel = batteryInfo.right?.level ?: 0
//    val caseLevel = batteryInfo.case?.level ?: 0
//    val leftCharging = batteryInfo.left?.isCharging ?: false
//    val rightCharging = batteryInfo.right?.isCharging ?: false
//    val caseCharging = batteryInfo.case?.isCharging ?: false
//
//    val prevLeft = previousBatteryStatus.find { it.component == BatteryComponent.LEFT }
//    val prevRight = previousBatteryStatus.find { it.component == BatteryComponent.RIGHT }
//    val prevCase = previousBatteryStatus.find { it.component == BatteryComponent.CASE }
//    val prevLeftCharging = prevLeft?.status == BatteryStatus.CHARGING
//    val prevRightCharging = prevRight?.status == BatteryStatus.CHARGING
//    val prevCaseCharging = prevCase?.status == BatteryStatus.CHARGING
//
//    var singleDisplayed by remember { mutableStateOf(false) }
//
//    Row {
//        Column (
//            modifier = Modifier
//                .fillMaxWidth(0.5f),
//            horizontalAlignment = Alignment.CenterHorizontally
//            if (batteryInfo.canDisplayBudsCombined)
//            Image (
//                bitmap = ImageBitmap.imageResource(resources.buds),
//                    batteryInfo.combinedBudsLevel,
//                    batteryInfo.combinedBudsCharging,
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            )
//            if (
//                leftCharging == rightCharging &&
//                (leftLevel - rightLevel) in -3..3
//            )
//            {
//                BatteryIndicator(
//                    leftLevel.coerceAtMost(rightLevel),
//                    leftCharging,
//                    if (leftLevel > 0 || batteryInfo.left?.isConnected == true) {
//                )
//                singleDisplayed = true
//            }
//            else {
//                singleDisplayed = false
//                Row (
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    if (leftLevel > 0 || left?.status != BatteryStatus.DISCONNECTED) {
//                    if (rightLevel > 0 || batteryInfo.right?.isConnected == true)
//                            leftLevel,
//                            leftCharging,
//                            "\uDBC6\uDCE5",
//                            previousCharging = prevLeftCharging
//                        )
//                    }
//                    if (leftLevel > 0 && rightLevel > 0)
//                    {
//                        Spacer(Modifier.width(16.dp))
//                    }
//                    if (rightLevel > 0 || right?.status != BatteryStatus.DISCONNECTED)
//                    {
//                        BatteryIndicator(
//                            rightLevel,
//                            rightCharging,
//                            "\uDBC6\uDCE8",
//                            previousCharging = prevRightCharging
//                        )
//                    }
//                }
//            }
//        }
//
//        Column (
//                if (caseLevel > 0 || batteryInfo.case?.isConnected == true) {
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Image(
//                bitmap = ImageBitmap.imageResource(resources.case),
//                contentDescription = stringResource(R.string.case_alt),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            )
//                if (caseLevel > 0 || case?.status != BatteryStatus.DISCONNECTED) {
//                    BatteryIndicator(
//                        caseLevel,
//                        caseCharging,
//                        prefix = if (!singleDisplayed) "\uDBC3\uDE6C" else "",
//                        previousCharging = prevCaseCharging
//                    )
//                }
//        }
//    }
}

//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Composable
//fun BatteryViewPreview() {
//    LibrePodsTheme {
//        Surface {
//            BatteryView(
//                model = AirPodsModel.AIRPODS_1
//            )
//        }
//    }
//}
//
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun BatteryViewDarkPreview() {
//    LibrePodsTheme {
//        Surface {
//            BatteryView(
//                model = AirPodsModel.AIRPODS_PRO_3
//            )
//        }
//    }
//}
