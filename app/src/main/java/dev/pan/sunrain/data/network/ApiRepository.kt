package dev.pan.sunrain.data.network

import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCurrentWeather(apiKey: String, location: String, aqi: String) =
        apiService.getCurrentWeather(apiKey, location, aqi)

}