package com.example.mapsnavigation.common.domain.repositories

import com.example.mapsnavigation.common.domain.model.UserLocation
import kotlinx.coroutines.flow.Flow

interface UserLocationsRepository {
    suspend fun saveUserLocation(userLocation: UserLocation)
    fun getUserLocationsFlow(): Flow<List<UserLocation>>
}