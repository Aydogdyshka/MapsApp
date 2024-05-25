package com.example.mapsnavigation.common.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsnavigation.common.domain.repositories.UserLocationsRepository
import com.example.mapsnavigation.common.presentation.model.UILocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val userLocationsRepository: UserLocationsRepository
): ViewModel(){

    private val _state: MutableStateFlow<UIState> = MutableStateFlow(UIState())
    val state = _state.asStateFlow()

    var locationUpdatesJob: Job? = null

    init {
        subscribeToLocationUpdates()
    }

    fun onEvent(event: MapsEvent) {
        when(event) {
            is MapsEvent.StartEvent -> {
                //subscribeToLocationUpdates()
            }
            is MapsEvent.StopEvent -> {
                unsubscribeFromUpdates()
            }
        }
    }

    private fun subscribeToLocationUpdates(){
        locationUpdatesJob = viewModelScope.launch {
            userLocationsRepository.getUserLocationsFlow()
                .map {
                    it.map {
                        val date = formatDateFromMillis(it.timeInMillis, FORMAT)
                        UILocation(
                            id = it.id,
                            date = date,
                            longitude = it.longitude.toString(),
                            latitude = it.latitude.toString()
                        )
                    }
                }
                .collect { list ->
                    _state.update { it.copy(list) }
                }
        }
    }

    private fun unsubscribeFromUpdates(){
        locationUpdatesJob?.cancel()
    }

    private fun formatDateFromMillis(timeInMillis: Long, format: String): String {
        val sdf = SimpleDateFormat(format)
        val date = Date(timeInMillis)
        return sdf.format(date)
    }

    companion object {
        const val FORMAT = "yyyy-MM-dd HH:mm:ss"
    }
}