package dev.pan.sunrain.data.network

import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCurrentWeather(apiKey: String, location: String, aqi: String) =
        apiService.getCurrentWeather(apiKey, location, aqi)

    suspend fun getForecastWeather(apiKey: String, location: String, days: Int, aqi: String) =
        apiService.getForecastWeather(apiKey, location, days, aqi)

    suspend fun searchWeather(apiKey: String, location: String) =
        apiService.searchWeather(apiKey, location)

}