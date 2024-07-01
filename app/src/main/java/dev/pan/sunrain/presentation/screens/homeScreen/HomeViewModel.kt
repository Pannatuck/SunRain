package dev.pan.sunrain.presentation.screens.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.pan.sunrain.data.network.ApiRepository
import dev.pan.sunrain.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        getCurrentWeather()
        getForecastWeather()
    }

    // TODO: location from edit field and enum for aqi
    fun getCurrentWeather() {
        viewModelScope.launch {
            repository.getCurrentWeather(
                apiKey = Constants.API_KEY, location = "Kyiv", aqi = "no"
            ).let {
                _homeState.update { state ->
                    /*you need to get response body, for it to work. For some reason when you get
                    * just some parameter of it, it's not working*/
                    // got, why it's not working (i'm idiot lmao), i tried to save value of another type into state
                    state.copy(currentWeather = it.body())
                }
            }
        }
    }

    fun getForecastWeather() {
        viewModelScope.launch {
            repository.getForecastWeather(
                apiKey = Constants.API_KEY, location = "Kyiv", days = 3, aqi = "no"
            ).let {
                _homeState.update { state ->
                    state.copy(forecastWeather = it.body())
                }
            }
        }
    }
}