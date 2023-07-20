package com.riza.example.explore.genres.state

import com.riza.example.explore.data.model.Genre

sealed interface GenresDisplayState {
    data class SuccessLoadGenres(
        val genres: List<Genre>
    ) : GenresDisplayState

    object Loading : GenresDisplayState
    data class ErrorLoadGenres(val message: String) : GenresDisplayState
}
    