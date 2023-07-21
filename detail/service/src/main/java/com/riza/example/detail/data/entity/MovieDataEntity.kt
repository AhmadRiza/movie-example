package com.riza.example.detail.data.entity

import androidx.annotation.Keep

@Keep
data class MovieDataEntity(
    val adult: Boolean?,
    val backdropPath: String?,
    val budget: Int?,
    val genres: List<GenreEntity>?,
    val id: Int?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double?,
    val posterPath: String?,
    val productionCountries: List<ProductionCountryEntity>?,
    val releaseDate: String?,
    val revenue: Int?,
    val runtime: Int?,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean?,
    val voteAverage: Double?,
    val voteCount: Int?
)

@Keep
data class GenreEntity(
    val id: Int?,
    val name: String?
)

@Keep
data class ProductionCountryEntity(
    val name: String?
)


