package com.riza.example.detail.data.entity

import androidx.annotation.Keep

@Keep
data class MovieTrailerDataEntity(
    val id: Int?,
    val results: List<TrailerEntity>?
)

@Keep
data class TrailerEntity(
    val name: String?,
    val key: String?,
    val site: String?,
    val size: Int?,
    val type: String?,
    val official: Boolean?,
    val publishedAt: String?,
    val id: String?
)

