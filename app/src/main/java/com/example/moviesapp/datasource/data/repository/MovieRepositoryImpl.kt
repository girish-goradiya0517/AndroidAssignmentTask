package com.example.moviesapp.datasource.data.repository

import android.annotation.SuppressLint
import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.util.Category
import com.example.moviesapp.util.Resource
import com.example.moviesapp.datasource.data.mappers.toMovie
import com.example.moviesapp.datasource.data.remote.services.MovieService
import com.example.moviesapp.datasource.domain.model.Movie
import com.example.moviesapp.datasource.domain.model.User
import com.example.moviesapp.datasource.domain.repository.MovieListRepository
import com.example.moviesapp.ui.core.presentation.paging.MoviePagingSource
import com.example.moviesapp.ui.core.presentation.paging.UserPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieService: MovieService,
) : MovieListRepository {

    override  fun getMoviePager(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 2, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(movieService) }
        ).flow
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getMovieList(
        category: String,
        page: Int,
    ): Flow<Resource<List<Movie>>> = flow {

        var code = 0
        emit(Resource.Loading(true))
        try {
            val movieListFromApi = movieService.getMovieList(category, page = page)
            code = movieListFromApi.code()
            val movieEntities = movieListFromApi.body()?.results?.map { movieDto ->
                movieDto.toMovie(category)
            }

            emit(Resource.Success(movieEntities))
        } catch (e: HttpException) {
            val message = when (code) {
                400 -> "Bad Request"
                401 -> "Unauthorized"
                403 -> "Forbidden"
                404 -> "Movies not found"
                500 -> "Server error"
                else -> "Unexpected error (code: ${code})"
            }
            emit(Resource.Error(message))
        } catch (e: IOException) {
            emit(Resource.Error("Network error. Please check your connection."))
        }
        catch (e: SocketTimeoutException) {
            emit(Resource.Error("Network error, Please try Again latter"))
        }
        catch (e: Exception) {
            emit(Resource.Error("Unexpected error occurred."))
        }

        emit(Resource.Loading(false))
    }

    override suspend fun getMovieById(id: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading(true))
        var code = 0
        try {
            val movieResponse = movieService
                .getMovieById(id.toString())

            val movieEntity = movieResponse.body()?.toMovie(Category.TRENDING)
            code = movieResponse.code()
            if (movieEntity != null) {
                emit(Resource.Success(movieEntity))
            } else {
                emit(Resource.Error("Movie not found"))
            }
        } catch (@SuppressLint("NewApi") e: HttpException) {
            val message = when (code) {
                400 -> "Bad Request"
                401 -> "Unauthorized"
                403 -> "Forbidden"
                404 -> "Movies not found"
                500 -> "Server error"
                else -> "Unexpected error (code: ${code})"
            }
            emit(Resource.Error(message))
        } catch (e: IOException) {
            emit(Resource.Error("Network error. Please check your connection."))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error occurred."))
        }
        emit(Resource.Loading(false))
    }

}
