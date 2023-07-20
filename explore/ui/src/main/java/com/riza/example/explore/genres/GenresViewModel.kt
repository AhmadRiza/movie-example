package com.riza.example.explore.genres

import androidx.lifecycle.viewModelScope
import com.riza.example.common.base.BaseViewModel
import com.riza.example.common.di.IODispatcher
import com.riza.example.explore.data.model.Genre
import com.riza.example.explore.data.usecase.GetMovieGenres
import com.riza.example.explore.genres.state.GenresDisplayState
import com.riza.example.explore.navigator.GenreDetailIntentParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by ahmadriza on 18/07/23.
 */
class GenresViewModel @Inject constructor(
    private val getMovieGenres: GetMovieGenres,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
): BaseViewModel<GenresViewModel.Intent, GenresViewModel.State, GenresViewModel.Effect>(
    State()
) {
    data class State (
        val displayState: GenresDisplayState = GenresDisplayState.Loading
    )

    sealed interface Intent {
        object OnViewCreated: Intent
        object OnTryAgain: Intent
        data class OnGenreClick(val genre: Genre): Intent
    }


    sealed interface Effect {
        data class OpenGenreDetail(val intentParam: GenreDetailIntentParam): Effect
    }

    override fun onIntentReceived(intent: Intent) {
        when(intent) {
            Intent.OnViewCreated -> onViewCreated()
            is Intent.OnGenreClick -> onGenreClick(intent.genre)
            Intent.OnTryAgain -> onTryAgain()
        }
    }

    private fun onTryAgain() {
        viewModelScope.launch { loadGenres() }
    }

    private fun onGenreClick(genre: Genre) {
        val intentParam = GenreDetailIntentParam(
            genreId = genre.id, genreName = genre.name, genreIcon = genre.emoticon
        )
        setEffect(Effect.OpenGenreDetail(intentParam))
    }

    private fun onViewCreated() {
        viewModelScope.launch { loadGenres() }
    }

    private suspend fun loadGenres() {
        setState { copy(displayState = GenresDisplayState.Loading) }
        when (val result = withContext(ioDispatcher) { getMovieGenres() }) {
            GetMovieGenres.GetGenresResult.Empty,
            is GetMovieGenres.GetGenresResult.Error -> {
                setState {
                    copy(displayState = GenresDisplayState.ErrorLoadGenres)
                }
            }
            is GetMovieGenres.GetGenresResult.Success -> {
                setState {
                    copy(displayState = GenresDisplayState.SuccessLoadGenres(result.genres))
                }
            }
        }
    }
}