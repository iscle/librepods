package me.kavishdevar.librepods.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.kavishdevar.librepods.domain.model.AirPodsInfo
import me.kavishdevar.librepods.domain.repository.AirPodsRepository
import javax.inject.Inject

class AirPodsRepositoryImpl @Inject constructor() : AirPodsRepository {
    override val airPodsInfo: Flow<AirPodsInfo> = flowOf(AirPodsInfo())
}

