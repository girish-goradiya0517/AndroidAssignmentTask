package com.example.moviesapp.ui.core.presentation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.util.UserPagingException
import com.example.moviesapp.datasource.data.mappers.toUser
import com.example.moviesapp.datasource.data.remote.services.UserService
import com.example.moviesapp.datasource.domain.model.User
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserPagingSource @Inject constructor(
     private val userService: UserService
) : PagingSource<Int, User>() {

    private var totalPages: Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1
        return try {
            val response = userService.getUsers(page = page)

            if (totalPages == null) {
                totalPages = response.total_pages
            }

            LoadResult.Page(
                data = response.data.map { it.toUser() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < totalPages!!) page + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(UserPagingException.NetworkException(e))

        } catch (e: HttpException) {
            LoadResult.Error(UserPagingException.HttpErrorException(e))

        } catch (e: Exception) {
            LoadResult.Error(UserPagingException.UnknownException(e))
        }
    }


    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { state.closestPageToPosition(it)?.prevKey?.plus(1) }
    }
}
