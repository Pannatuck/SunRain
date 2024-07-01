package dev.pan.sunrain.presentation.screens.homeScreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.permissionx.guolindev.PermissionX
import dev.pan.sunrain.data.network.models.current.CurrentWeather
import dev.pan.sunrain.util.getWeekdayName
import java.time.LocalTime

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState
) {


    Column {
        CurrentWeather(state = state)
        HourlyWeatherList(state = state)
        WeatherForecast(state = state)
        LocationScreen()
    }
}

@Composable
fun LocationHandler(
    onLocationResult: (Location?) -> Unit
) {
    val context = LocalContext.current
    val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var permissionDialogQueue by remember { mutableStateOf(listOf<String>()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val allGranted = permissionsMap.values.all { it }
        if (allGranted) {
            getCurrentLocation(context, onLocationResult)
        } else {
            val deniedList = permissionsMap.filter { !it.value }.keys
            permissionDialogQueue = deniedList.toList()
            onLocationResult(null)
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(permissions.toTypedArray())
    }

    permissionDialogQueue.forEach { permission ->
        PermissionDialog(
            permission = permission,
            onDismiss = { permissionDialogQueue = permissionDialogQueue - permission },
            onOkClick = {
                permissionDialogQueue = permissionDialogQueue - permission
                // Here you could open app settings if needed
            }
        )
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(context: Context, onLocationResult: (Location?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val cancellationTokenSource = CancellationTokenSource()

    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
        .addOnSuccessListener { location ->
            onLocationResult(location)
        }
        .addOnFailureListener { exception ->
            onLocationResult(null)
            showToast(context, "Failed to get location: ${exception.message}")
        }
}

@Composable
fun LocationScreen() {
    var location by remember { mutableStateOf<Location?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LocationHandler { result ->
            location = result
        }

        if (location != null) {
            Text("Latitude: ${location?.latitude}")
            Text("Longitude: ${location?.longitude}")
        } else {
            Text("Location not available")
        }
    }
}

@Composable
fun PermissionDialog(
    permission: String,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permission required") },
        text = { Text("This app needs $permission permission to function properly.") },
        confirmButton = {
            Button(onClick = onOkClick) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

@Composable
fun CurrentWeather(
    modifier: Modifier = Modifier,
    state: HomeState
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 20.dp,
        modifier = modifier
            .padding(top = 50.dp, bottom = 20.dp)
            .fillMaxWidth()
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = state.currentWeather?.location?.name ?: "unknown",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = (state.currentWeather?.current?.temp_c.toString() + "°C") ?: "unknown",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = state.currentWeather?.current?.condition?.text ?: "unknown",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun HourlyWeatherList(
    state: HomeState
) {
    /* Get current time of location
    *  Cut out its hour: part from it (with :)
    *  Find element in array of hours that has that hour in it
    *  Use that element as first in list*/


    // TODO: put that stuff in ViewModel
    // TODO: change lazy to stateFlow

    // current time in hour
    val apiCurrentTime by lazy { getHoursFromString(state.forecastWeather?.location?.localtime.toString()) }

    val currentTime = state.forecastWeather?.location?.localtime


    // filtering element that match with current time
    val firstElementIndex = state.forecastWeather?.forecast?.forecastday?.get(0)?.hour?.indexOfFirst { hour ->
        hour.time.contains(apiCurrentTime)
    }
    // to start list from needed index of hour
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = firstElementIndex ?: 0)

    val listOfElements = state.forecastWeather?.forecast?.forecastday?.get(0)?.hour ?: listOf()


// TODO: Show first element of hours Row starting from current time

    Text(
        modifier = Modifier.padding(8.dp),
        text = "Today's hourly weather",
        style = MaterialTheme.typography.headlineSmall
    )

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
    ) {
        itemsIndexed(listOfElements){index, item ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = listOfElements[index].time.split(" ")[1],
                    )
                    AsyncImage(
                        model = "https:${listOfElements[index].condition.icon}",
                        contentDescription = "weather icon",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${item.temp_c}°C"
                    )

                }
            }

        }
    }
}


fun getHoursFromString(dateTimeString: String): String {
    // Split the string at the space to separate date and time
    val timePart = dateTimeString.split(" ")[1]

    // Split the time at the colon and take the first part (hours)
    val hoursString = timePart.dropLast(2)

    return hoursString
}


@Composable
fun WeatherForecast(state: HomeState) {
    Column(
        modifier = Modifier.padding(top = 24.dp, start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //Text("Debug: Number of forecast days: ${state.forecastWeather?.forecast?.forecastday?.size ?: 0}")

        Text(
            text = "Forecast weather",
            style = MaterialTheme.typography.headlineSmall
        )

        state.forecastWeather?.forecast?.forecastday?.take(3)?.forEachIndexed { index, forecastday ->
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = getWeekdayName(forecastday.date)
                )
                Text(
                    text = "Avg: " + forecastday.day.avgtemp_c.toString()
                )
                Text(
                    text = "Min: " + forecastday.day.mintemp_c.toString()
                )
                Text(
                    text = "Max: " + forecastday.day.maxtemp_c.toString()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview(
    state: HomeState = HomeState()
) {
    HomeScreen(state = state)
}