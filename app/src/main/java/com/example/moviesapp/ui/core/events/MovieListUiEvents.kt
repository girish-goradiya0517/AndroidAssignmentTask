package com.example.moviesapp.ui.core.events

sealed interface MovieListUiEvent {
    data class Paginate(val category: String) : MovieListUiEvent
}