package com.example.mapsnavigation.common.di

import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.mapsnavigation.R
import com.example.mapsnavigation.common.platform.services.ForegroundLocationService.Companion.NOTIFICATION_CHANNEL_ID
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext app: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

    @Singleton
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext app: Context
    ) = NotificationCompat.Builder(app, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_SOUND)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setVibrate(LongArray(0))
        .setContentTitle(app.getString(R.string.location_tracking))
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

}