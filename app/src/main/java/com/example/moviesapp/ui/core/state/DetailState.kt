package com.example.moviesapp.ui.core.state

import com.example.moviesapp.datasource.domain.model.Movie

data class DetailState(
    val isLoading:Boolean = false,
    val movie:Movie? = null
)
