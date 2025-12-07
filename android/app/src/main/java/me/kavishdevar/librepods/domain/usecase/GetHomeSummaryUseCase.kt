package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.AppSettings
import me.kavishdevar.librepods.domain.model.DeviceState
import me.kavishdevar.librepods.domain.model.HearingProfile
import me.kavishdevar.librepods.domain.repository.AudioRepository
import me.kavishdevar.librepods.domain.repository.DeviceRepository
import me.kavishdevar.librepods.domain.repository.SettingsRepository

class GetHomeSummaryUseCase(
    private val deviceRepository: DeviceRepository,
    private val audioRepository: AudioRepository,
    private val settingsRepository: SettingsRepository
) {
    data class HomeSummary(
        val deviceState: DeviceState,
        val hearingProfile: HearingProfile?,
        val appSettings: AppSettings,
    )
    suspend operator fun invoke(): HomeSummary {
        return HomeSummary(
            deviceState = deviceRepository.getDeviceState(),
            hearingProfile = audioRepository.getHearingProfile(),
            appSettings = settingsRepository.getAppSettings(),
        )
    }
}

