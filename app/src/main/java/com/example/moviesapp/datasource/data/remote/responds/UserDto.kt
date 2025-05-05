package com.example.moviesapp.datasource.data.remote.responds

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserDto(
    @SerialName("page")
    val page: Int,
    @SerialName("per_page")
    val per_page: Int,
    val total: Int,
    @SerialName("total_pages")
    val total_pages: Int,

    @SerialName("data")
    val data: List<UserSubData>
)

@Serializable
data class UserSubData(
    val id: Int,
    val email: String,
    val first_name: String,
    val last_name: String,
    val avatar: String
)
