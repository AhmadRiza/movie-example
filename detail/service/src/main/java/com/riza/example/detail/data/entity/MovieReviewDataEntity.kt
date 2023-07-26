package com.riza.example.detail.data.entity

import androidx.annotation.Keep

@Keep
data class MovieReviewDataEntity(
    val id: Int?,
    val page: Int?,
    val results: List<ReviewEntity>?,
    val totalPages: Int?,
    val totalResults: Int?
)

@Keep
data class ReviewEntity(
    val author: String?,
    val authorDetails: AuthorDetailsEntity?,
    val content: String?,
    val createdAt: String?,
    val id: String?,
    val updatedAt: String?,
    val url: String?
)

@Keep
data class AuthorDetailsEntity(
    val name: String?,
    val username: String?,
    val avatarPath: String?,
    val rating: Int?
)
