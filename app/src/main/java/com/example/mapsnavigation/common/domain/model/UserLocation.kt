package com.example.mapsnavigation.common.domain.model

data class UserLocation(
    val id: Int,
    val longitude: Double,
    val latitude: Double,
    val timeInMillis: Long
)