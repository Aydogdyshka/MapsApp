package com.example.mapsnavigation.common.di

import androidx.room.Insert
import com.example.mapsnavigation.common.data.repositories.MapsUserLocationRepo
import com.example.mapsnavigation.common.domain.repositories.UserLocationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class AppBindsModule {
    @Binds
    abstract fun bindRepo(
        userLocationRepo: MapsUserLocationRepo
    ): UserLocationsRepository
}