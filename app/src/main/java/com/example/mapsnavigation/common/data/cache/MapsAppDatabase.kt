package com.example.mapsnavigation.common.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mapsnavigation.common.data.cache.daos.CachedUserLocationDao
import com.example.mapsnavigation.common.data.cache.model.CachedUserLocation

@Database(
    entities = [
        CachedUserLocation::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MapsAppDatabase: RoomDatabase() {
    abstract fun userLocationDao(): CachedUserLocationDao
}