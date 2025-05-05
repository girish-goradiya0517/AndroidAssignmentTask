package com.example.moviesapp.ui.core.presentation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.datasource.data.mappers.toMovie
import com.example.moviesapp.datasource.data.remote.services.MovieService
import com.example.moviesapp.datasource.domain.model.Movie
import com.example.moviesapp.util.Category
import com.example.moviesapp.util.UserPagingException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MoviePagingSource @Inject constructor(
    private val movieService: MovieService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 1
        return try {
            val response = movieService.getMovieList(page = page)
            if (response.isSuccessful) {
                val body = response.body()
                val data = body?.results?.map { it.toMovie(Category.TRENDING) } ?: emptyList()
                val nextKey = if ((body?.page ?: 0) < (body?.total_pages ?: 0)) page + 1 else null

                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            LoadResult.Error(UserPagingException.NetworkException(e))

        } catch (e: HttpException) {
            LoadResult.Error(UserPagingException.HttpErrorException(e))

        } catch (e: Exception) {
            LoadResult.Error(UserPagingException.UnknownException(e))
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { state.closestPageToPosition(it)?.prevKey?.plus(1) }
    }
}
