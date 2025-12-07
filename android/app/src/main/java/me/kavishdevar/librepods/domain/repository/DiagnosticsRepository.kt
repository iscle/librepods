package me.kavishdevar.librepods.domain.repository

interface DiagnosticsRepository {
    suspend fun collectLogs(): String
}

