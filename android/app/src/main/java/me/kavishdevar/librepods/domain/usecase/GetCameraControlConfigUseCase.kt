package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.CameraRepository

class GetCameraControlConfigUseCase(
    private val cameraRepository: CameraRepository
) {
    suspend operator fun invoke(): Map<String, Any> = cameraRepository.getCameraConfig()
}

