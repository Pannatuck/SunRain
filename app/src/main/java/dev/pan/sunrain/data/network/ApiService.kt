package dev.pan.sunrain.data.network

import dev.pan.sunrain.data.network.models.current.CurrentWeather
import dev.pan.sunrain.data.network.models.forecast.Forecast
import dev.pan.sunrain.data.network.models.search.Search
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// possible calls
/*Location (name, region(?), location_time(?)*/
/*Temp, condition(text, icon), wind_kph, humidity, feelslike_c*/
interface ApiService {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("aqi") aqi: String
    ): Response<CurrentWeather>


    // https://api.weatherapi.com/v1//forecast.json?key=260a64e4f72c413b9d182620242206&q=London&aqi=no
    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int,
        @Query("aqi") aqi: String
    ): Response<Forecast>

    // http://api.weatherapi.com/v1/search.json?key=260a64e4f72c413b9d182620242206&q=Lond
    @GET("search.json")
    suspend fun searchWeather(
        @Query("key") apiKey: String,
        @Query("q") location: String
    ): Response<List<Search>>


}