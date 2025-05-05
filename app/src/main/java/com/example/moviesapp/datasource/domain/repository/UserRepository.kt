package com.example.moviesapp.datasource.domain.repository

import androidx.paging.PagingData
import com.example.moviesapp.util.Resource
import com.example.moviesapp.datasource.data.local.entity.UserEntity
import com.example.moviesapp.datasource.data.remote.responds.AddUserResponse
import com.example.moviesapp.datasource.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserPager(): Flow<PagingData<User>>

    suspend fun addNewUser(name:String,jobTitle:String) : Flow<Resource<AddUserResponse>>

    suspend fun insertUserLocally(user: UserEntity)
}