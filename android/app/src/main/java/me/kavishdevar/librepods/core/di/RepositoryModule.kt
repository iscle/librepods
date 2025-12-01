package me.kavishdevar.librepods.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.kavishdevar.librepods.data.datastore.OnboardingPreferencesRepositoryImpl
import me.kavishdevar.librepods.data.repository.AirPodsRepositoryImpl
import me.kavishdevar.librepods.domain.repository.AirPodsRepository
import me.kavishdevar.librepods.domain.repository.OnboardingPreferencesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideOnboardingPreferencesRepository(
        @ApplicationContext context: Context
    ): OnboardingPreferencesRepository {
        return OnboardingPreferencesRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideAirPodsRepository(
        airPodsRepositoryImpl: AirPodsRepositoryImpl
    ): AirPodsRepository {
        return airPodsRepositoryImpl
    }
}

