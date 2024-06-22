package dev.pan.sunrain.presentation.screens.homeScreen

import dev.pan.sunrain.data.network.models.CurrentWeather

data class HomeState(
    val currentWeather: CurrentWeather? = null
//    val isLoading: Boolean = true,
//    val error: String? = null
)