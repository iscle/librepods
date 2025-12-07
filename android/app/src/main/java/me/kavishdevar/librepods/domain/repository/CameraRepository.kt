package me.kavishdevar.librepods.domain.repository

interface CameraRepository {
    suspend fun getCameraConfig(): Map<String, Any>
    suspend fun setCameraControl(action: String): Boolean
}

