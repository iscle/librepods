package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.CameraRepository

class SetCameraControlUseCase(
    private val cameraRepository: CameraRepository
) {
    suspend operator fun invoke(params: String): Boolean =
        cameraRepository.setCameraControl(params)
}

