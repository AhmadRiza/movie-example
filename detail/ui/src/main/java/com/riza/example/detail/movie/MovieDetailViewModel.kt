package com.riza.example.detail.movie

import com.riza.example.detail.movie.state.MovieDetailItemModel

/**
 * Created by ahmadriza on 21/07/23.
 */
class MovieDetailViewModel {

    data class State(
        val displayItems: List<MovieDetailItemModel> = emptyList()
    )

}