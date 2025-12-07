package me.kavishdevar.librepods.domain.repository

import me.kavishdevar.librepods.domain.model.VersionInfo

interface VersionRepository {
    suspend fun getVersionInfo(): VersionInfo
}

