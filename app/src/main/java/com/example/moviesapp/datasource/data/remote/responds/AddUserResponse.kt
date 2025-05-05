package com.example.moviesapp.datasource.data.remote.responds

import kotlinx.serialization.Serializable


@Serializable
data class AddUserResponse(
    val name: String? = null,
    val job: String? = null,
    val id: String? = null,
    val createdAt: String? = null
)
