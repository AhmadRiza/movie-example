package com.riza.example.explore.genredetail.state

/**
 * Created by ahmadriza on 20/07/23.
 */

sealed class GenreDetailItemModel {

    abstract val key: String

    object LoadMore: GenreDetailItemModel() {
        override val key: String
            get() = "LoadMore"
    }

    data class Movie(
        val id: Int,
        val thumbnail: String,
        val title: String,
        val rating: String,
        val releaseDate: String
    ): GenreDetailItemModel() {
        override val key: String
            get() = "Movie:$id"
    }
}