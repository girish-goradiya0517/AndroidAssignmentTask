package com.example.moviesapp.datasource.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesapp.util.Resource
import com.example.moviesapp.datasource.data.local.dao.UserDao
import com.example.moviesapp.datasource.data.local.entity.UserEntity
import com.example.moviesapp.datasource.data.remote.request.AddUserRequest
import com.example.moviesapp.datasource.data.remote.responds.AddUserResponse
import com.example.moviesapp.datasource.data.remote.services.UserService
import com.example.moviesapp.datasource.domain.model.User
import com.example.moviesapp.datasource.domain.repository.UserRepository
import com.example.moviesapp.ui.core.presentation.paging.UserPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val userDao: UserDao
) : UserRepository {

    override fun getUserPager(): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 6, enablePlaceholders = false),
            pagingSourceFactory = { UserPagingSource(userService) }
        ).flow
    }

    override suspend fun addNewUser(name: String, jobTitle: String): Flow<Resource<AddUserResponse>> {
        val request = AddUserRequest(name, jobTitle)
        val response = userService.addNewUser(request = request)
        return if (response != null) {
            flow { emit(Resource.Success(response)) }
        } else {
            flow { emit(Resource.Error("Error While Loading data")) }
        }
    }

    override suspend fun insertUserLocally(user: UserEntity) {
        userDao.insertUser(user)
    }
}
