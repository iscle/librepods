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

package me.kavishdevar.librepods.domain.model

/**
 * Domain model representing battery information for a specific AirPods component.
 *
 * @property component The component this battery info belongs to (LEFT, RIGHT, or CASE)
 * @property level The battery level percentage (0-100)
 * @property status The current charging/connection status of the component
 */
data class ComponentBatteryInfo(
    val component: Component,
    val level: Int,
    val status: Status
) {
    /**
     * Represents a component of the AirPods device.
     */
    enum class Component {
        LEFT,
        RIGHT,
        CASE
    }

    /**
     * Represents the battery status of a component.
     */
    enum class Status {
        /** Component is charging */
        CHARGING,
        /** Component is not charging */
        NOT_CHARGING,
        /** Component is disconnected/not in use */
        DISCONNECTED
    }

    /**
     * Whether this component is currently charging.
     */
    val isCharging: Boolean
        get() = status == Status.CHARGING

    /**
     * Whether this component is currently connected.
     */
    val isConnected: Boolean
        get() = status != Status.DISCONNECTED
}

/**
 * Domain model representing the complete battery state for AirPods.
 * This model provides convenient access to individual component battery information
 * and aggregated state for the UI.
 *
 * @property components List of battery information for each component
 */
data class BatteryInfo(
    val components: List<ComponentBatteryInfo> = emptyList()
) {
    /**
     * Battery information for the left earbud, or null if not available.
     */
    val left: ComponentBatteryInfo?
        get() = components.find { it.component == ComponentBatteryInfo.Component.LEFT }

    /**
     * Battery information for the right earbud, or null if not available.
     */
    val right: ComponentBatteryInfo?
        get() = components.find { it.component == ComponentBatteryInfo.Component.RIGHT }

    /**
     * Battery information for the case, or null if not available.
     */
    val case: ComponentBatteryInfo?
        get() = components.find { it.component == ComponentBatteryInfo.Component.CASE }

    /**
     * Whether both earbuds have similar battery levels (within 3% difference).
     * Returns false if either earbud is missing.
     */
    val areBudsBalanced: Boolean
        get() {
            val leftLevel = left?.level ?: return false
            val rightLevel = right?.level ?: return false
            return (leftLevel - rightLevel) in -3..3
        }

    /**
     * Whether both earbuds have the same charging status.
     * Returns false if either earbud is missing.
     */
    val areBothBudsCharging: Boolean
        get() {
            val leftCharging = left?.isCharging ?: return false
            val rightCharging = right?.isCharging ?: return false
            return leftCharging && rightCharging
        }

    /**
     * Whether both earbuds have the same charging status (both charging or both not charging).
     * Returns false if either earbud is missing.
     */
    val budsHaveSameChargingStatus: Boolean
        get() {
            val leftCharging = left?.isCharging ?: return false
            val rightCharging = right?.isCharging ?: return false
            return leftCharging == rightCharging
        }

    /**
     * Whether the battery levels and charging status are similar enough
     * to display a single combined indicator for both buds.
     */
    val canDisplayBudsCombined: Boolean
        get() = budsHaveSameChargingStatus && areBudsBalanced

    /**
     * The combined battery level for buds when displaying them together.
     * Returns the lower of the two levels, or 0 if not available.
     */
    val combinedBudsLevel: Int
        get() {
            val leftLevel = left?.level ?: 0
            val rightLevel = right?.level ?: 0
            return leftLevel.coerceAtMost(rightLevel)
        }

    /**
     * Whether the combined buds indicator should show charging status.
     */
    val combinedBudsCharging: Boolean
        get() = areBothBudsCharging
}

