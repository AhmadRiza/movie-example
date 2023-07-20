package com.riza.example.explore.genredetail.state

/**
 * Created by ahmadriza on 20/07/23.
 */

sealed interface GenreDetailItemModel {

    object ErrorMore: GenreDetailItemModel
    object InitialShimmer: GenreDetailItemModel
    object InitialError: GenreDetailItemModel
    object LoadMore: GenreDetailItemModel

    data class Movie(
        val id: Int,
        val thumbnail: String,
        val title: String,
        val rating: String,
        val releaseDate: String
    ): GenreDetailItemModel
}