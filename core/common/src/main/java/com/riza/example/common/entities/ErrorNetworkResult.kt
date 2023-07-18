package com.riza.example.common.entities

sealed interface ErrorNetworkResult : NetworkResult<Nothing> {
    data class NetworkError(
        val errorMessage: String,
        val httpCode: Int,
    ) : ErrorNetworkResult

    data class EmptyErrorResponse(
        val httpCode: Int,
    ) : ErrorNetworkResult

    object NoInternetConnection : ErrorNetworkResult

    data class UnknownError(val cause: String) : ErrorNetworkResult
}
