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
 * Domain model representing AirPods models with their specifications.
 * Contains only business logic data, no UI resources.
 */
enum class AirPodsModel(
    val modelNumbers: List<String>,
    val modelName: String,
    val displayName: String,
    val manufacturer: String,
    val capabilities: Set<Capability>
) {
    AIRPODS_1(
        modelNumbers = listOf("A1523", "A1722"),
        modelName = "AirPods 1",
        displayName = "AirPods",
        manufacturer = "Apple Inc.",
        capabilities = emptySet()
    ),

    AIRPODS_2(
        modelNumbers = listOf("A2032", "A2031"),
        modelName = "AirPods 2",
        displayName = "AirPods",
        manufacturer = "Apple Inc.",
        capabilities = emptySet()
    ),

    AIRPODS_3(
        modelNumbers = listOf("A2565", "A2564"),
        modelName = "AirPods 3",
        displayName = "AirPods",
        manufacturer = "Apple Inc.",
        capabilities = setOf(
            Capability.HEAD_GESTURES
        )
    ),

    AIRPODS_4(
        modelNumbers = listOf("A3053", "A3050", "A3054"),
        modelName = "AirPods 4",
        displayName = "AirPods",
        manufacturer = "Apple Inc.",
        capabilities = setOf(
            Capability.HEAD_GESTURES,
            Capability.SLEEP_DETECTION,
            Capability.ADAPTIVE_VOLUME
        )
    ),

    AIRPODS_4_ANC(
        modelNumbers = listOf("A3056", "A3055", "A3057"),
        modelName = "AirPods 4 (ANC)",
        displayName = "AirPods",
        manufacturer = "Apple Inc.",
        capabilities = setOf(
            Capability.LISTENING_MODE,
            Capability.CONVERSATION_AWARENESS,
            Capability.HEAD_GESTURES,
            Capability.ADAPTIVE_AUDIO,
            Capability.SLEEP_DETECTION,
            Capability.ADAPTIVE_VOLUME
        )
    ),

    AIRPODS_PRO_1(
        modelNumbers = listOf("A2084", "A2083"),
        modelName = "AirPods Pro 1",
        displayName = "AirPods Pro",
        manufacturer = "Apple Inc.",
        capabilities = setOf(
            Capability.LISTENING_MODE
        )
    ),

    AIRPODS_PRO_2_LIGHTNING(
        modelNumbers = listOf("A2931", "A2699", "A2698"),
        modelName = "AirPods Pro 2 with MagSafe Charging Case (Lightning)",
        displayName = "AirPods Pro",
        manufacturer = "Apple Inc.",
        capabilities = setOf(
            Capability.LISTENING_MODE,
            Capability.CONVERSATION_AWARENESS,
            Capability.STEM_CONFIG,
            Capability.LOUD_SOUND_REDUCTION,
            Capability.SLEEP_DETECTION,
            Capability.HEARING_AID,
            Capability.ADAPTIVE_AUDIO,
            Capability.ADAPTIVE_VOLUME,
            Capability.SWIPE_FOR_VOLUME
        )
    ),

    AIRPODS_PRO_2_USBC(
        modelNumbers = listOf("A3047", "A3048", "A3049"),
        modelName = "AirPods Pro 2 with MagSafe Charging Case (USB-C)",
        displayName = "AirPods Pro",
        manufacturer = "Apple Inc.",
        capabilities = setOf(
            Capability.LISTENING_MODE,
            Capability.CONVERSATION_AWARENESS,
            Capability.STEM_CONFIG,
            Capability.LOUD_SOUND_REDUCTION,
            Capability.SLEEP_DETECTION,
            Capability.HEARING_AID,
            Capability.ADAPTIVE_AUDIO,
            Capability.ADAPTIVE_VOLUME,
            Capability.SWIPE_FOR_VOLUME
        )
    ),

    AIRPODS_PRO_3(
        modelNumbers = listOf("A3063", "A3064", "A3065"),
        modelName = "AirPods Pro 3",
        displayName = "AirPods Pro",
        manufacturer = "Apple Inc.",
        capabilities = setOf(
            Capability.LISTENING_MODE,
            Capability.CONVERSATION_AWARENESS,
            Capability.HEAD_GESTURES,
            Capability.STEM_CONFIG,
            Capability.LOUD_SOUND_REDUCTION,
            Capability.PPE,
            Capability.SLEEP_DETECTION,
            Capability.HEARING_AID,
            Capability.ADAPTIVE_AUDIO,
            Capability.ADAPTIVE_VOLUME,
            Capability.SWIPE_FOR_VOLUME,
            Capability.HRM
        )
    );

    companion object {
        /**
         * Find an AirPods model by its model number.
         * @param modelNumber The model number to search for (e.g., "A2084")
         * @return The matching AirPodsModel, or null if not found
         */
        fun fromModelNumber(modelNumber: String): AirPodsModel? {
            return entries.find { modelNumber in it.modelNumbers }
        }

        /**
         * Get all available AirPods models.
         */
        fun all(): List<AirPodsModel> = entries
    }
}

/**
 * Device capabilities that different AirPods models support.
 */
enum class Capability {
    LISTENING_MODE,
    CONVERSATION_AWARENESS,
    STEM_CONFIG,
    HEAD_GESTURES,
    LOUD_SOUND_REDUCTION,
    PPE,
    SLEEP_DETECTION,
    HEARING_AID,
    ADAPTIVE_AUDIO,
    ADAPTIVE_VOLUME,
    SWIPE_FOR_VOLUME,
    HRM
}

