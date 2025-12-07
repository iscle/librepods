package me.kavishdevar.librepods.domain.repository

interface FirmwareRepository {
    suspend fun hasUpdateAvailable(): Boolean
    suspend fun startUpdate(): Boolean
}

