package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.model.BudSide
import me.kavishdevar.librepods.domain.repository.DeviceRepository

class RenameEarbudUseCase(
    private val deviceRepository: DeviceRepository
) {
    data class Params(val side: BudSide, val name: String)
    suspend operator fun invoke(params: Params): Boolean {
        // TODO: validate name
        return deviceRepository.rename(params.side, params.name)
    }
}

