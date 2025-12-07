package me.kavishdevar.librepods.domain.usecase

import me.kavishdevar.librepods.domain.repository.LicenseRepository

class GetOpenSourceLicensesUseCase(
    private val licenseRepository: LicenseRepository
) {
    suspend operator fun invoke(): List<String> = licenseRepository.getLicenses()
}

