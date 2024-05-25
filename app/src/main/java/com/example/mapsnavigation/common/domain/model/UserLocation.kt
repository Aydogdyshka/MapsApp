package com.example.mapsnavigation.common.domain.model

data class UserLocation(
    val id: Int = 0,
    val longitude: Double,
    val latitude: Double,
    val timeInMillis: Long
)