package dev.pan.sunrain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.pan.sunrain.presentation.screens.Screen
import dev.pan.sunrain.presentation.screens.homeScreen.HomeScreen
import dev.pan.sunrain.presentation.screens.splashScreen.SplashScreen
import dev.pan.sunrain.presentation.screens.homeScreen.HomeViewModel
import dev.pan.sunrain.ui.theme.SunRainTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SunRainTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash
                )
                {
                    composable<Screen.Splash> {
                        SplashScreen(navController)
                    }
                    composable<Screen.Home> { backStackEntry ->
                        val viewModel = hiltViewModel<HomeViewModel>()
                        val state = viewModel.homeState.value

                        HomeScreen(state = state.copy(

                        ))
//                        val args = backStackEntry.toRoute<Screen.Home>() // receive arguments passed to this screen if navigated through a route
//                        Column(
//                            modifier = Modifier.fillMaxSize(),
//                            verticalArrangement = Arrangement.Center,
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Text(text = "Received ${args.text} from ${args.id} screen." +
//                                    "Also, ${args.user?.name} is ${args.user?.age} years old and lives in ${args.user?.address}")
//                        }
                    }
                }
            }
        }
    }
}

