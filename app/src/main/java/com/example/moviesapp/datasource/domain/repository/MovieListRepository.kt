package com.example.moviesapp.datasource.domain.repository

import androidx.paging.PagingData
import com.example.moviesapp.util.Resource
import com.example.moviesapp.datasource.domain.model.Movie
import com.example.moviesapp.datasource.domain.model.User
import kotlinx.coroutines.flow.*

interface MovieListRepository {

    suspend fun getMovieList(
        category: String,
        page:Int
    ):Flow<Resource<List<Movie>>>

    suspend fun getMovieById(id:Int):Flow<Resource<Movie>>

     fun getMoviePager():Flow<PagingData<Movie>>

}