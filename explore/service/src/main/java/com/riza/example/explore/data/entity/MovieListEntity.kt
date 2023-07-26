package com.riza.example.explore.data.entity

import androidx.annotation.Keep

/**
 * Created by ahmadriza on 19/07/23.
 */
@Keep
data class MovieListEntity(
    val page: Int?,
    val results: List<MovieResult>?,
    val totalPages: Int?,
    val totalResults: Int?
) {
    @Keep
    data class MovieResult(
        val adult: Boolean?,
        val backdropPath: String?,
        val genreIds: List<Int>?,
        val id: Int?,
        val originalLanguage: String?,
        val originalTitle: String?,
        val overview: String?,
        val popularity: Double?,
        val posterPath: String?,
        val releaseDate: String?,
        val title: String?,
        val video: Boolean?,
        val voteAverage: Double?,
        val voteCount: Int?
    )
}
