package com.riza.example.common.entities

data class ErrorEntity(val message: String) {
    companion object {
        fun empty() = ErrorEntity("")
    }
}
