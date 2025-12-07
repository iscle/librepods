package me.kavishdevar.librepods.domain.repository

import me.kavishdevar.librepods.domain.model.BudSide
import me.kavishdevar.librepods.domain.model.DeviceState
import me.kavishdevar.librepods.domain.model.TransparencySettings

interface DeviceRepository {
    suspend fun getDeviceState(): DeviceState
    suspend fun rename(side: BudSide, name: String): Boolean
    suspend fun setLongPressAction(side: BudSide, action: String): Boolean
    suspend fun supportsLongPress(): Boolean
    suspend fun setHeadTrackingEnabled(enabled: Boolean): Boolean
    suspend fun calibrateHeadTracking(): Boolean
    suspend fun applyTransparency(settings: TransparencySettings): Boolean
}
