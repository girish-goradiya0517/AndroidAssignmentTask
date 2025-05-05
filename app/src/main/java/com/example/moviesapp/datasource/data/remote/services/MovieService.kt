package com.example.moviesapp.datasource.data.remote.services


import com.example.moviesapp.datasource.data.remote.responds.MovieDto
import com.example.moviesapp.datasource.data.remote.responds.MovieListDto
import com.example.moviesapp.util.Category
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("{category}/movie/day")
    suspend fun getMovieList(
        @Path("category") category: String = Category.TRENDING,
        @Query("language") lang:String = "en-US",
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieListDto>

    @GET("movie/{movieId}")
    suspend fun getMovieById(
        @Path("movieId") movieId: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MovieDto>

    companion object {
        const val MOVIE_BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "4b536e0a1aa263372543cb4a980607bd"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"
    }
}
