package com.example.moviesapp.datasource.data.remote.request

import kotlinx.serialization.Serializable


@Serializable
data class AddUserRequest(
    val name:String,
    val job : String
)
