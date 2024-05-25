package com.example.mapsnavigation.common.platform.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.mapsnavigation.R
import com.example.mapsnavigation.common.domain.model.UserLocation
import com.example.mapsnavigation.common.domain.repositories.UserLocationsRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundLocationService: LifecycleService() {

    @Inject
    lateinit var userLocationsRepository: UserLocationsRepository

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    private lateinit var currentNotificationBuilder: NotificationCompat.Builder

    private lateinit var notificationManager: NotificationManager
    private lateinit var locationRequest: LocationRequest

    var serviceKilled = false
    var isFirstRun: Boolean = true

    private val trackingCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
                result.locations.let { locations ->
                    for (location in locations) {
                        saveLocation(location)
                        notificationManager.notify(
                            NOTIFICATION_ID,
                            generateNotification(location)
                        )
                    }
                }

        }
    }

    private fun saveLocation(location: Location?){
        lifecycleScope.launch {
            userLocationsRepository.saveUserLocation(
                UserLocation(
                    latitude = location?.latitude ?: 0.0,
                    longitude = location?.longitude ?: 0.0,
                    timeInMillis = location?.time ?: 0L
                )
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        locationRequest = LocationRequest.Builder(LOCATION_UPDATE_INTERVAL)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        currentNotificationBuilder = baseNotificationBuilder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            when(it.action){
                ACTION_START_SERVICE -> {
                    startForegroundService()
                    subscribeToLocationUpdates()
                }
                ACTION_STOP_SERVICE -> {
                    unsubscribeToLocationUpdates()
                    killService()
                }
                else -> {}
            }
        }
        return START_STICKY
    }

    private fun subscribeToLocationUpdates() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                trackingCallback,
                Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    private fun unsubscribeToLocationUpdates() {
        try {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(trackingCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Location Callback removed.")
                    //stopSelf()
                } else {
                    Log.d(TAG, "Failed to remove Location Callback.")
                }
            }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    private fun killService() {
        isFirstRun = true
        serviceKilled = true
        unsubscribeToLocationUpdates()
        stopForeground(true)
        stopSelf()
    }

    private fun startForegroundService(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(R.string.app_name),
            NotificationManager.IMPORTANCE_LOW
        )
        notificationChannel.vibrationPattern = LongArray(0)
        notificationChannel.enableVibration(false)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun generateNotification(location: Location?): Notification {
        val body = if(location != null){
            getString(R.string.current_notification) + " " + location.latitude + ", " + location.longitude
        } else {
            getString(R.string.update_location)
        }

        return currentNotificationBuilder
            .setContentText(body)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        unsubscribeToLocationUpdates()
    }

    companion object {
        const val LOCATION_UPDATE_INTERVAL = 30000L
        const val TAG = "ForegroundLocationService"
        const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
        const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID_MAPS"
        const val NOTIFICATION_ID = 2233
    }
}