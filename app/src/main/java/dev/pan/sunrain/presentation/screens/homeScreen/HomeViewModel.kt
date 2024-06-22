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
    }

    // TODO: location from edit field and enum for aqi
    fun getCurrentWeather() {
        viewModelScope.launch {
            repository.getCurrentWeather(
                apiKey = Constants.API_KEY, location = "London", aqi = "no"
            ).let {
                if (it.isSuccessful) {
                    _homeState.update { currentState ->
                        currentState.copy(
                            currentWeather = currentState.currentWeather
                        )
                    }
                } else {
                    Log.d("HomeViewModelError", "MESSAGE ${it.body()?.current?.temp_c} ")
                }
            }
        }
    }
}