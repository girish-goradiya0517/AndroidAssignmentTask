package com.example.moviesapp.ui.core.activitiy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.moviesapp.ui.core.presentation.DetailsScreen
import com.example.moviesapp.ui.core.presentation.HomeScreen
import com.example.moviesapp.ui.core.presentation.userscreen.UserScreen
import com.example.moviesapp.ui.core.route.Screen
import com.example.moviesapp.ui.theme.MoviesAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAppTheme {
                SetBarColor(color = MaterialTheme.colorScheme.inverseOnSurface)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val NavController = rememberNavController()
                    NavHost(
                        navController = NavController,
                        startDestination = Screen.UserScreen.route
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(navController = NavController)
                        }
                        composable(Screen.UserScreen.route) {
                            UserScreen(onClick = {
                                NavController.navigate(Screen.Home.route)
                            })
                        }
                        composable(
                            Screen.Details.route + "/{movieId}",
                            arguments = listOf(
                                navArgument("movieId") { type = NavType.IntType }
                            )
                        ) { _ ->
                            DetailsScreen()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController(

        )
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }
}

