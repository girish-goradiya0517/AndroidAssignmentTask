package com.example.moviesapp.util

sealed class UserPagingException(message: String?, cause: Throwable?) : Exception(message, cause) {

    class NetworkException(cause: Throwable?) : UserPagingException("Network error please try after sometime", cause)

    class HttpErrorException(cause: Throwable?) : UserPagingException("HTTP error please try after sometime", cause)

    class UnknownException(cause: Throwable?) : UserPagingException("Unknown error please try after sometime", cause)
}
