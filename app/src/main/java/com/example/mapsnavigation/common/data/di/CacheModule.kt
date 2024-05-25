package com.example.mapsnavigation.common.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mapsnavigation.common.data.cache.MapsAppDatabase
import com.example.mapsnavigation.common.data.cache.daos.CachedUserLocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CacheModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MapsAppDatabase {
        return Room.databaseBuilder(
            context,
            MapsAppDatabase::class.java,
            "MapsAppDatabase.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDao(
        database: MapsAppDatabase
    ): CachedUserLocationDao {
        return database.userLocationDao()
    }
}