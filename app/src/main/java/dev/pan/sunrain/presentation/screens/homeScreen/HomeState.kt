package dev.pan.sunrain.presentation.screens.homeScreen

import dev.pan.sunrain.data.network.models.current.CurrentWeather
import dev.pan.sunrain.data.network.models.forecast.Forecast
import dev.pan.sunrain.data.network.models.search.Search

data class HomeState(
    val currentWeather: CurrentWeather? = null,
    val forecastWeather: Forecast? = null,
    val searchWeather: List<Search>? = null,

)