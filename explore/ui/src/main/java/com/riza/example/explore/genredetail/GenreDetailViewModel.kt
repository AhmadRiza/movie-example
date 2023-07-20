package com.riza.example.explore.genredetail

import androidx.lifecycle.viewModelScope
import com.riza.example.common.base.BaseViewModel
import com.riza.example.common.di.IODispatcher
import com.riza.example.explore.data.model.Genre
import com.riza.example.explore.data.usecase.GetMovieGenres
import com.riza.example.explore.data.usecase.GetMoviesByGenre
import com.riza.example.explore.genredetail.GenreDetailViewModel.State
import com.riza.example.explore.genredetail.state.GenreDetailItemModel
import com.riza.example.explore.genredetail.state.GenreDetailItemModel.Movie
import com.riza.example.explore.genres.state.GenresDisplayState
import com.riza.example.explore.navigator.GenreDetailIntentParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by ahmadriza on 20/07/23.
 */
class GenreDetailViewModel @Inject constructor(
    private val getMoviesByGenre: GetMoviesByGenre,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): BaseViewModel<GenreDetailViewModel.Intent, State, GenreDetailViewModel.Effect>(
    State()
) {
    data class State (
        val title: String = "",
        val items: List<GenreDetailItemModel> = emptyList()
    )

    sealed interface Intent {
        data class OnViewCreated(val intentParam: GenreDetailIntentParam): Intent
    }


    class Effect

    override fun onIntentReceived(intent: Intent) {
        when(intent) {
            is Intent.OnViewCreated -> onViewCreated(intent.intentParam)
        }
    }

    private fun onViewCreated(intentParam: GenreDetailIntentParam) {
        viewModelScope.launch {
            val params = GetMoviesByGenre.Params(
                page = 1, genreId = intentParam.genreId
            )
            when (val result = withContext(ioDispatcher) { getMoviesByGenre(params) }) {
                GetMoviesByGenre.GetMoviesByGenreResult.Empty -> {}
                is GetMoviesByGenre.GetMoviesByGenreResult.Error -> {}
                is GetMoviesByGenre.GetMoviesByGenreResult.Success -> {
                    val movies = result.movies.map {
                        Movie(
                            id = it.id,
                            thumbnail = "https://image.tmdb.org/t/p/w500${it.posterPath}",
                            title = it.title,
                            rating = "${it.voteAverage}/10",
                            releaseDate = it.releaseDate
                        )
                    }
                    setState {
                        copy(
                            title = "${intentParam.genreIcon} ${intentParam.genreName}",
                            items = movies
                        )
                    }
                }
            }

        }
    }
}