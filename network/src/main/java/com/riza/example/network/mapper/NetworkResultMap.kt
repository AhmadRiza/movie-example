package com.riza.example.network.mapper

import com.google.gson.Gson
import com.riza.example.common.entities.ErrorEntity
import com.riza.example.common.entities.ErrorNetworkResult
import com.riza.example.common.entities.NetworkResult
import com.riza.example.network.exception.NoInternetConnectionException
import java.io.Reader
import retrofit2.Response

suspend fun <T : Any> safeApiCall(apiCall: suspend () -> NetworkResult<T>): NetworkResult<T> {
    return try {
        apiCall()
    } catch (error: Throwable) {
        when (error) {
            is NoInternetConnectionException -> {
                ErrorNetworkResult.NoInternetConnection
            }
            else -> {
                ErrorNetworkResult.UnknownError(error.message.orEmpty())
            }
        }
    }
}

fun <T : Any> Response<T>.toNetworkResult(): NetworkResult<T> {
    val rawResponse: okhttp3.Response = raw()
    return if (isSuccessful) {
        body().toSuccessResult(code())
    } else {
        val stream = errorBody()?.charStream()
        val errorResponse = makeErrorResponse(stream)
        errorResponse.toErrorResult(code())
    }
}

private fun <T : Any> T?.toSuccessResult(
    httpCode: Int,
): NetworkResult.Success<T> {
    return if (this != null) {
        NetworkResult.Success.WithData(this)
    } else {
        NetworkResult.Success.EmptyData(httpCode)
    }
}

private fun ErrorEntity?.toErrorResult(
    httpStatusCode: Int,
): ErrorNetworkResult {
    return if (this != null && message.isNotEmpty()) {
        ErrorNetworkResult.NetworkError(
            errorMessage = message,
            httpCode = httpStatusCode
        )
    } else {
        ErrorNetworkResult.EmptyErrorResponse(
            httpCode = httpStatusCode
        )
    }
}

private fun makeErrorResponse(charStream: Reader?): ErrorEntity? {
    if (charStream == null) return ErrorEntity.empty()

    return Gson().fromJson(charStream, ErrorEntity::class.java)
}
