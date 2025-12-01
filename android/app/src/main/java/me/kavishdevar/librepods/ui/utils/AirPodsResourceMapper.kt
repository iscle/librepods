/*
    LibrePods - AirPods liberated from Apple's ecosystem
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

package me.kavishdevar.librepods.ui.utils

import androidx.annotation.DrawableRes
import me.kavishdevar.librepods.R
import me.kavishdevar.librepods.domain.model.AirPodsModel

/**
 * UI resources for AirPods models.
 * Separates presentation concerns from domain logic.
 */
data class AirPodsResources(
    @DrawableRes val budCase: Int,
    @DrawableRes val buds: Int,
    @DrawableRes val leftBud: Int,
    @DrawableRes val rightBud: Int,
    @DrawableRes val case: Int
)

/**
 * Maps AirPods domain models to their UI resources.
 */
object AirPodsResourceMapper {
    /**
     * Get drawable resources for a given AirPods model.
     */
    fun getResources(model: AirPodsModel): AirPodsResources {
        return when (model) {
            AirPodsModel.AIRPODS_1 -> AirPodsResources(
                budCase = R.drawable.airpods_1,
                buds = R.drawable.airpods_1_buds,
                leftBud = R.drawable.airpods_1_left,
                rightBud = R.drawable.airpods_1_right,
                case = R.drawable.airpods_1_case
            )

            AirPodsModel.AIRPODS_2 -> AirPodsResources(
                budCase = R.drawable.airpods_2,
                buds = R.drawable.airpods_2_buds,
                leftBud = R.drawable.airpods_2_left,
                rightBud = R.drawable.airpods_2_right,
                case = R.drawable.airpods_2_case
            )

            AirPodsModel.AIRPODS_3 -> AirPodsResources(
                budCase = R.drawable.airpods_3,
                buds = R.drawable.airpods_3_buds,
                leftBud = R.drawable.airpods_3_left,
                rightBud = R.drawable.airpods_3_right,
                case = R.drawable.airpods_3_case
            )

            AirPodsModel.AIRPODS_4 -> AirPodsResources(
                budCase = R.drawable.airpods_4,
                buds = R.drawable.airpods_4_buds,
                leftBud = R.drawable.airpods_4_left,
                rightBud = R.drawable.airpods_4_right,
                case = R.drawable.airpods_4_case
            )

            AirPodsModel.AIRPODS_4_ANC -> AirPodsResources(
                budCase = R.drawable.airpods_4,
                buds = R.drawable.airpods_4_buds,
                leftBud = R.drawable.airpods_4_left,
                rightBud = R.drawable.airpods_4_right,
                case = R.drawable.airpods_4_case
            )

            AirPodsModel.AIRPODS_PRO_1 -> AirPodsResources(
                budCase = R.drawable.airpods_pro_1,
                buds = R.drawable.airpods_pro_1_buds,
                leftBud = R.drawable.airpods_pro_1_left,
                rightBud = R.drawable.airpods_pro_1_right,
                case = R.drawable.airpods_pro_1_case
            )

            AirPodsModel.AIRPODS_PRO_2_LIGHTNING -> AirPodsResources(
                budCase = R.drawable.airpods_pro_2,
                buds = R.drawable.airpods_pro_2_buds,
                leftBud = R.drawable.airpods_pro_2_left,
                rightBud = R.drawable.airpods_pro_2_right,
                case = R.drawable.airpods_pro_2_case
            )

            AirPodsModel.AIRPODS_PRO_2_USBC -> AirPodsResources(
                budCase = R.drawable.airpods_pro_2,
                buds = R.drawable.airpods_pro_2_buds,
                leftBud = R.drawable.airpods_pro_2_left,
                rightBud = R.drawable.airpods_pro_2_right,
                case = R.drawable.airpods_pro_2_case
            )

            AirPodsModel.AIRPODS_PRO_3 -> AirPodsResources(
                budCase = R.drawable.airpods_pro_3,
                buds = R.drawable.airpods_pro_3_buds,
                leftBud = R.drawable.airpods_pro_3_left,
                rightBud = R.drawable.airpods_pro_3_right,
                case = R.drawable.airpods_pro_3_case
            )
        }
    }
}

/**
 * Extension property to easily access resources from an AirPodsModel.
 */
val AirPodsModel.resources: AirPodsResources
    get() = AirPodsResourceMapper.getResources(this)

