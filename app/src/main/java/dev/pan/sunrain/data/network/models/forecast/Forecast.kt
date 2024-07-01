package dev.pan.sunrain.data.network.models.forecast

data class Forecast(
    val current: Current,
    val forecast: ForecastX,
    val location: Location
)