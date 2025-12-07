package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.VersionInfo
import me.kavishdevar.librepods.domain.repository.VersionRepository

class GetVersionInfoUseCase(
    private val versionRepository: VersionRepository
) {
    suspend operator fun invoke(): VersionInfo = versionRepository.getVersionInfo()
}

