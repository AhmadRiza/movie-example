package com.riza.example.common.model

import com.riza.example.common.entities.ErrorNetworkResult
import com.riza.example.common.entities.NetworkResult

sealed class Result<out T : Any> {
    sealed class Success<T : Any> : Result<T>() {
        val value: T?
            get() = when (this) {
                is Empty -> null
                is WithData -> data
            }

        val nextValue: String?
            get() = when (this) {
                is Empty -> null
                is WithData -> next
            }

        data class WithData<T : Any>(val data: T, val next: String = "") : Success<T>()
        data class Empty(val httpCode: Int) : Success<Nothing>()
    }

    data class Error(
        val errorMessage: String,
        val httpCode: Int = 0,
    ) : Result<Nothing>() {

        companion object {
            fun noInternetConnection(): Error {
                // TODO: Move to string res
                return Error(
                    errorMessage = DefaultErrorMessage.NO_INTERNET
                )
            }

            fun unknown(): Error {
                // TODO: Move to string res
                return Error(
                    errorMessage = DefaultErrorMessage.UNKNOWN_ERROR,
                )
            }
        }
    }
}

fun ErrorNetworkResult.toErrorResult(): Result.Error {
    return when (this) {
        is ErrorNetworkResult.EmptyErrorResponse -> {
            Result.Error(
                errorMessage = "Terjadi kesalahan, coba beberapa saat lagi ya.",
                httpCode = httpCode,
            )
        }
        is ErrorNetworkResult.NetworkError -> {
            Result.Error(errorMessage)
        }
        is ErrorNetworkResult.NoInternetConnection -> {
            Result.Error.noInternetConnection()
        }
        is ErrorNetworkResult.UnknownError -> {
            Result.Error.unknown()
        }
    }
}

fun NetworkResult.Success.EmptyData.toEmptyResult(): Result.Success.Empty {
    return Result.Success.Empty(
        httpCode = httpCode
    )
}
