package com.example.mapsnavigation.common.presentation.views

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsnavigation.R
import com.example.mapsnavigation.common.platform.services.ForegroundLocationService
import com.example.mapsnavigation.common.presentation.MapsEvent
import com.example.mapsnavigation.common.presentation.MapsViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.image.ImageProvider

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapView(
    mapsViewModel: MapsViewModel = viewModel()
){
    val context = LocalContext.current
    val map = rememberMapViewLifecycle()
    val state by mapsViewModel.state.collectAsState()

    val locationPermissionsState = rememberPermissionState(
        permission = android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ granted ->
        if (granted) {

        } else {
            Toast.makeText(context, "Location permission is not given", Toast.LENGTH_LONG).show()
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {

        }
    )

    LaunchedEffect(true){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.fillMaxSize()) {
            if (state.locations.isNotEmpty()){
                LocationListView(
                    modifier = Modifier.weight(1f),
                    locations = state.locations
                )
            }
            AndroidView(
                modifier = Modifier.weight(1f),
                factory = {
                    map.apply {}
                },
                update = { mapView ->
                    state.locations.let {
                        Log.d("Called", "MapView: $it")
                        it.forEach { location ->
                            mapView
                                .mapWindow
                                .map
                                .mapObjects
                                .addCircle(
                                    Circle(
                                        Point(
                                            location.latitude.toDouble(),
                                            location.longitude.toDouble()
                                        ),
                                        4f
                                    )
                                )
                        }

                        if (it.isNotEmpty()) {
                            mapView.mapWindow.map.move(
                                CameraPosition(
                                    Point(
                                        it.last().latitude.toDouble(),
                                        it.last().longitude.toDouble()
                                    ),
                                    18f,
                                    0f,
                                    0f
                                ),
                                Animation(Animation.Type.LINEAR, 1f),
                                null
                            )
                        }
                    }

                }
            )
            if (locationPermissionsState.status.isGranted){
                StartButton {
                    mapsViewModel.onEvent(event = MapsEvent.StartEvent)
                    context.sendCommandToService(ForegroundLocationService.ACTION_START_SERVICE)
                }
            } else {
                NoPermissionScreen {
                    locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }
}

private fun Context.sendCommandToService(action: String){
    Intent(this, ForegroundLocationService::class.java).also { intent ->
        intent.action = action
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}


private fun Context.startLocationUpdateService(){
    Intent(this, ForegroundLocationService::class.java).also { intent ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}