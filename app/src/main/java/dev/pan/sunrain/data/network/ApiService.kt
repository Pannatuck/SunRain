package dev.pan.sunrain.data.network

import dev.pan.sunrain.data.network.models.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// possible calls
/*Location (name, region(?), location_time(?)*/
/*Temp, condition(text, icon), wind_kph, humidity, feelslike_c*/
interface ApiService {

    @GET("/current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("aqi") aqi: String
    ): Response<CurrentWeather>


}