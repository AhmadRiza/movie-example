package com.riza.example.explore.genres

import androidx.lifecycle.viewModelScope
import com.riza.example.common.base.BaseViewModel
import com.riza.example.common.di.IODispatcher
import com.riza.example.explore.data.usecase.GetMovieGenres
import com.riza.example.explore.genres.state.GenresDisplayState
import kotlinx.coroutines.CoroutineDispatcher
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
    }


    class Effect

    override fun onIntentReceived(intent: Intent) {
        when(intent) {
            Intent.OnViewCreated -> onViewCreated()
        }
    }

    private fun onViewCreated() {
        viewModelScope.launch {
            setState { copy(displayState = GenresDisplayState.Loading) }
            when (val result = withContext(ioDispatcher) { getMovieGenres() }) {
                GetMovieGenres.GetGenresResult.Empty -> {
                    setState { copy(displayState = GenresDisplayState.ErrorLoadGenres("Empty")) }
                }
                is GetMovieGenres.GetGenresResult.Error -> {
                    setState {
                        copy(displayState = GenresDisplayState.ErrorLoadGenres(result.message))
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
}