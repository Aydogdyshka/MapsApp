package com.example.mapsnavigation.common.data.repositories

import com.example.mapsnavigation.common.data.cache.daos.CachedUserLocationDao
import com.example.mapsnavigation.common.data.cache.model.CachedUserLocation
import com.example.mapsnavigation.common.domain.model.UserLocation
import com.example.mapsnavigation.common.domain.repositories.UserLocationsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MapsUserLocationRepo @Inject constructor(
    private val userLocationDao: CachedUserLocationDao
): UserLocationsRepository {
    override suspend fun saveUserLocation(userLocation: UserLocation) {
       userLocationDao.insert(CachedUserLocation.fromDomain(userLocation))
    }

    override fun getUserLocationsFlow(): Flow<List<UserLocation>> {
       return userLocationDao.getUserLocationsFlow().map { it.map { it.toDomain() }}
    }
}