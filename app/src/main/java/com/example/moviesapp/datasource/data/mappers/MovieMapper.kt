package com.example.moviesapp.datasource.data.mappers

import com.example.moviesapp.datasource.data.local.entity.MovieEntity
import com.example.moviesapp.datasource.data.remote.responds.MovieDto

import com.example.moviesapp.datasource.data.remote.responds.UserSubData
import com.example.moviesapp.datasource.domain.model.Movie
import com.example.moviesapp.datasource.domain.model.User


fun MovieDto.toMovieEntity(
    category: String
): MovieEntity {
    return MovieEntity(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        id = id ?: -1,
        original_title = original_title ?: "",
        video = video ?: false,

        category = category,

        genre_ids = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        }
    )
}

fun MovieDto.toMovie(
    category: String
): Movie {
    return Movie(
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        video = video ?: false,
        id = id ?: -1,
        adult = adult?:false,
        original_title = original_title?:"",

        category = category,

        genre_ids = try {
            genre_ids?.toString()?.split(",")?.map { it.toInt() }?: listOf(-1,-2)
        } catch (e: Exception) {
            listOf(-1, -2)
        }
    )
}


fun UserSubData.toUser(): User {
    return User(
        id = this.id,
        email, first_name, last_name, avatar
    )
}