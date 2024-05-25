package com.example.mapsnavigation.common.platform.services

import android.app.NotificationManager
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForegroundLocationService: LifecycleService() {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var notificationManager: NotificationManager
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    var serviceKilled = false

    private val trackingCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            super.onLocationResult(location)

        }
    }

    private fun postInitialValues(){
        isTracking.postValue(false)
    }

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        const val LOCATION_UPDATE_INTERVAL = 30000L
        const val TAG = "ForegroundLocationService"
    }
}