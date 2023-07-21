package com.riza.example.detail.movie.state

sealed interface MovieDetailItemModel {

    sealed interface Detail: MovieDetailItemModel {
        object Loading: Detail
        data class Success(
            val title: String,
            val thumbnail: String,
            val releaseDate: String,
            val countries: String,
            val genres: List<Genre>
        ) : Detail {
            data class Genre(val name: String, val id: Int)
        }
        object Error: Detail
    }

    sealed interface Overview: MovieDetailItemModel {
        object Loading: Overview
        object Error: Overview
        data class Success(val overview: String) : Overview
    }

    sealed interface Trailers: MovieDetailItemModel {
        object Loading: Trailers
        object Error: Trailers
        data class Success(
            val trailers: List<Video>
        ): Trailers {
            data class Video(
                val thumbnail: String,
                val youtubeKey: String,
                val title: String,
                val id: String
            )
        }
    }

    sealed interface ReviewTitle: MovieDetailItemModel {
        object Loading: ReviewTitle
        object Error: ReviewTitle
        data class Success(val average: String, val total: String): ReviewTitle
    }

    object LoadMoreReview : MovieDetailItemModel
    object ErrorLoadMoreReview: MovieDetailItemModel
    data class Review(
        val id: String,
        val username: String,
        val time: String,
        val review: Int,
        val description: String
    ) : MovieDetailItemModel

}