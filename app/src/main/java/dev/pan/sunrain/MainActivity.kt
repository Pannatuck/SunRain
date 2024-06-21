package dev.pan.sunrain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.pan.sunrain.presentation.screens.HomeScreen
import dev.pan.sunrain.presentation.screens.Screen
import dev.pan.sunrain.presentation.screens.SplashScreen
import dev.pan.sunrain.ui.theme.SunRainTheme


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
                        HomeScreen()
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

