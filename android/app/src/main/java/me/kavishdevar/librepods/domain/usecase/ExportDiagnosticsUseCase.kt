package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.DeviceRepository
import me.kavishdevar.librepods.domain.repository.DiagnosticsRepository

class ExportDiagnosticsUseCase(
    private val diagnosticsRepository: DiagnosticsRepository,
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(): String {
        val logs = diagnosticsRepository.collectLogs()
        val state = deviceRepository.getDeviceState()
        return "$logs\nState: $state"
    }
}

