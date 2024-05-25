package com.example.mapsnavigation.common.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mapsnavigation.common.data.cache.model.CachedUserLocation
import kotlinx.coroutines.flow.Flow


@Dao
interface CachedUserLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cachedUserLocation: CachedUserLocation)

    @Query("SELECT * FROM cacheduserlocation ORDER BY timeInMillis")
    fun getUserLocationsFlow(): Flow<List<CachedUserLocation>>
}