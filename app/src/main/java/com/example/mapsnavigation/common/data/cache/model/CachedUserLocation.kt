package com.example.mapsnavigation.common.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mapsnavigation.common.domain.model.UserLocation

@Entity
data class CachedUserLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val longitude: Double,
    val latitude: Double,
    val timeInMillis: Long
) {
    companion object {
        fun fromDomain(domainModel: UserLocation): CachedUserLocation {
            return CachedUserLocation(
                longitude = domainModel.longitude,
                latitude = domainModel.latitude,
                timeInMillis = domainModel.timeInMillis
            )
        }
    }

    fun toDomain(): UserLocation {
        return UserLocation(id, longitude, latitude, timeInMillis)
    }
}