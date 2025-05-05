package com.example.moviesapp.datasource.data.remote.responds

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MovieListDto(
    val page: Int,
    val results: List<MovieDto>,
    @SerialName("total_pages")
    val total_pages: Int? = null,
    @SerialName("total_results")
    val total_results: Int? = null
)
