package com.riza.example.detail.data.model

import com.riza.example.detail.data.entity.ReviewEntity

data class Review(
    val id: String,
    val username: String,
    val time: String,
    val rating: Int,
    val content: String
)
