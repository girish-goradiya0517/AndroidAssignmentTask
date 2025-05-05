package com.example.moviesapp.datasource.data.remote.services

import com.example.moviesapp.datasource.data.remote.request.AddUserRequest
import com.example.moviesapp.datasource.data.remote.responds.AddUserResponse
import com.example.moviesapp.datasource.data.remote.responds.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface UserService {

    @GET
    suspend fun getUsers(
        @Url url: String = "${USER_BASE_URL}api/users",
        @Query("page") page: Int = 1,
        @Header("x-api-key") apiKey: String = USER_API_KEY
    ): UserDto

    @POST
    @Headers("Content-Type: application/json")
    suspend fun addNewUser(
        @Url url: String = "${USER_BASE_URL}api/users",
        @Body request: AddUserRequest,
        @Header("x-api-key") apiKey: String = USER_API_KEY
    ): AddUserResponse

    companion object {
        const val USER_BASE_URL = "https://reqres.in/"
        const val USER_API_KEY = "reqres-free-v1"
    }

}
