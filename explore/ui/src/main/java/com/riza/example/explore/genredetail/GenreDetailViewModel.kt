package com.riza.example.explore.genredetail

import androidx.lifecycle.viewModelScope
import com.riza.example.common.base.BaseViewModel
import com.riza.example.common.di.IODispatcher
import com.riza.example.explore.data.model.MovieItem
import com.riza.example.explore.data.usecase.GetGenreEmoticon
import com.riza.example.explore.data.usecase.GetMoviesByGenre
import com.riza.example.explore.genredetail.GenreDetailViewModel.State
import com.riza.example.explore.genredetail.state.GenreDetailItemModel
import com.riza.example.explore.genredetail.state.GenreDetailItemModel.Movie
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
    private val getGenreEmoticon: GetGenreEmoticon,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<GenreDetailViewModel.Intent, State, GenreDetailViewModel.Effect>(
    State()
) {
    data class State(
        val title: String = "",
        val items: List<GenreDetailItemModel> = emptyList()
    )

    sealed interface Intent {
        data class OnViewCreated(val intentParam: GenreDetailIntentParam) : Intent
        object LoadMoreMovies : Intent

        object RetryLoadMore : Intent

        object RetryInitialLoad : Intent
    }


    class Effect

    private var nextPageParam: GetMoviesByGenre.Params? = null

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.OnViewCreated -> onViewCreated(intent.intentParam)
            Intent.LoadMoreMovies -> onLoadMoreMovies()
            Intent.RetryInitialLoad -> onRetryInitialLoad()
            Intent.RetryLoadMore -> onRetryLoadMore()
        }
    }

    private fun onRetryLoadMore() {
        viewModelScope.launch {
            val updatedItems = movieOnlyItems.toMutableList()
            updatedItems.add(GenreDetailItemModel.LoadMore)
            setState { copy(items = updatedItems) }
        }
    }

    private fun onRetryInitialLoad() {
        viewModelScope.launch {
            setState {
                copy(items = getInitialShimmerItems())
            }
            loadMoreMovie()
        }
    }

    private fun onLoadMoreMovies() {
        viewModelScope.launch {
            loadMoreMovie()
        }
    }

    private fun onViewCreated(intentParam: GenreDetailIntentParam) {
        viewModelScope.launch {
            val genreEmoticon = getGenreEmoticon(intentParam.genreName)
            setState {
                copy(
                    title = "$genreEmoticon ${intentParam.genreName}",
                    items = getInitialShimmerItems()
                )
            }
            nextPageParam = GetMoviesByGenre.Params(
                page = 1, genreId = intentParam.genreId
            )
            loadMoreMovie()
        }
    }

    private suspend fun loadMoreMovie() {
        val params = nextPageParam ?: return
        when (val result = withContext(ioDispatcher) { getMoviesByGenre(params) }) {
            GetMoviesByGenre.GetMoviesByGenreResult.Empty -> {
                nextPageParam = null
                setState { copy(items = movieOnlyItems) }
            }

            is GetMoviesByGenre.GetMoviesByGenreResult.Error -> {
                val updatedItems = movieOnlyItems.toMutableList()
                val errorItem = if (params.page == 1) {
                    GenreDetailItemModel.InitialError
                } else {
                    GenreDetailItemModel.ErrorMore
                }
                updatedItems.add(errorItem)
                setState { copy(items = updatedItems.toList()) }
            }

            is GetMoviesByGenre.GetMoviesByGenreResult.Success -> {
                val movies = result.movies.map { it.toMovieModel() }
                val updatedItems = movieOnlyItems.toMutableList()
                updatedItems.addAll(movies)
                if (result.currentPage == result.totalPage) {
                    nextPageParam = null
                } else {
                    nextPageParam = GetMoviesByGenre.Params(
                        page = result.currentPage + 1, genreId = params.genreId
                    )
                    updatedItems.add(GenreDetailItemModel.LoadMore)
                }
                setState { copy(items = updatedItems.toList()) }

            }
        }
    }


    private fun MovieItem.toMovieModel(): Movie {
        return Movie(
            id = id,
            thumbnail = posterPath,
            title = title,
            rating = "${voteAverage}/10",
            releaseDate = releaseDate
        )
    }

    private fun getInitialShimmerItems(): List<GenreDetailItemModel> {
        val list = mutableListOf<GenreDetailItemModel>()
        repeat(8) { list.add(GenreDetailItemModel.InitialShimmer) }
        return list.toList()
    }

    private val movieOnlyItems: List<GenreDetailItemModel>
        get() = viewState.items.filterIsInstance<Movie>()
}