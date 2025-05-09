package com.example.moviesapp.ui.core.route

sealed class Screen(val route:String) {
    data object Home: Screen("main")
    data object Details : Screen("details")
    data object UserScreen:Screen("userScreen")
}