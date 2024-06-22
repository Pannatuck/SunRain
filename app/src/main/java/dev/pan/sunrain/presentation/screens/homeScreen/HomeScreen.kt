package dev.pan.sunrain.presentation.screens.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState
) {
    
    Box(modifier.padding(16.dp)) {
        Text(text = "Weather temperature: ${state.currentWeather?.current?.temp_c}")
    }
}