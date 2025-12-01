package me.kavishdevar.librepods.domain.repository

import kotlinx.coroutines.flow.Flow
import me.kavishdevar.librepods.domain.model.AirPodsInfo

interface AirPodsRepository {
    val airPodsInfo: Flow<AirPodsInfo>
}
