package com.riza.example.detail.data.model

data class MovieDetail(
    val title: String,
    val thumbnail: String,
    val genres: List<Genre>,
    val overview: String,
    val releaseDate: String,
    val countries: String,
    val voteCount: Int,
    val voteAverage: Double
) {
    data class Genre(
        val id: Int,
        val name: String
    )
}
