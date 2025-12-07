package me.kavishdevar.librepods.domain.repository

interface LicenseRepository {
    suspend fun getLicenses(): List<String>
}

