package com.example.mapsnavigation.common.presentation

import com.example.mapsnavigation.common.presentation.model.UILocation

data class UIState(
    val locations: List<UILocation> = emptyList(),
)