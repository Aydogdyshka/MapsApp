package com.example.mapsnavigation.common.presentation

//MVI patter intent (event)
sealed class MapsEvent {
    data object StartEvent: MapsEvent()
    data object StopEvent: MapsEvent()
}