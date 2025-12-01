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

package me.kavishdevar.librepods.domain.mapper

import me.kavishdevar.librepods.constants.Battery
import me.kavishdevar.librepods.constants.BatteryComponent
import me.kavishdevar.librepods.constants.BatteryStatus
import me.kavishdevar.librepods.domain.model.BatteryInfo
import me.kavishdevar.librepods.domain.model.ComponentBatteryInfo

/**
 * Mapper to convert between data layer Battery models and domain layer BatteryInfo models.
 */
object BatteryMapper {

    /**
     * Maps a Battery component constant to a ComponentBatteryInfo.Component enum.
     */
    private fun mapComponent(component: Int): ComponentBatteryInfo.Component? {
        return when (component) {
            BatteryComponent.LEFT -> ComponentBatteryInfo.Component.LEFT
            BatteryComponent.RIGHT -> ComponentBatteryInfo.Component.RIGHT
            BatteryComponent.CASE -> ComponentBatteryInfo.Component.CASE
            else -> null
        }
    }

    /**
     * Maps a Battery status constant to a ComponentBatteryInfo.Status enum.
     */
    private fun mapStatus(status: Int): ComponentBatteryInfo.Status {
        return when (status) {
            BatteryStatus.CHARGING -> ComponentBatteryInfo.Status.CHARGING
            BatteryStatus.NOT_CHARGING -> ComponentBatteryInfo.Status.NOT_CHARGING
            BatteryStatus.DISCONNECTED -> ComponentBatteryInfo.Status.DISCONNECTED
            else -> ComponentBatteryInfo.Status.DISCONNECTED
        }
    }

    /**
     * Converts a single Battery data model to a ComponentBatteryInfo domain model.
     * Returns null if the component type is unknown.
     */
    fun toDomain(battery: Battery): ComponentBatteryInfo? {
        val component = mapComponent(battery.component) ?: return null
        return ComponentBatteryInfo(
            component = component,
            level = battery.level,
            status = mapStatus(battery.status)
        )
    }

    /**
     * Converts a list of Battery data models to a BatteryInfo domain model.
     * Filters out any batteries with unknown component types.
     */
    fun toDomain(batteries: List<Battery>): BatteryInfo {
        val components = batteries.mapNotNull { toDomain(it) }
        return BatteryInfo(components = components)
    }

    /**
     * Converts a ComponentBatteryInfo domain model to a Battery data model.
     */
    fun toData(info: ComponentBatteryInfo): Battery {
        val component = when (info.component) {
            ComponentBatteryInfo.Component.LEFT -> BatteryComponent.LEFT
            ComponentBatteryInfo.Component.RIGHT -> BatteryComponent.RIGHT
            ComponentBatteryInfo.Component.CASE -> BatteryComponent.CASE
        }
        val status = when (info.status) {
            ComponentBatteryInfo.Status.CHARGING -> BatteryStatus.CHARGING
            ComponentBatteryInfo.Status.NOT_CHARGING -> BatteryStatus.NOT_CHARGING
            ComponentBatteryInfo.Status.DISCONNECTED -> BatteryStatus.DISCONNECTED
        }
        return Battery(
            component = component,
            level = info.level,
            status = status
        )
    }

    /**
     * Converts a BatteryInfo domain model to a list of Battery data models.
     */
    fun toData(info: BatteryInfo): List<Battery> {
        return info.components.map { toData(it) }
    }
}

