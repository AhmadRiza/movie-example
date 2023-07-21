package com.riza.example.detail.movie

import androidx.lifecycle.viewModelScope
import com.riza.example.common.base.BaseViewModel
import com.riza.example.common.di.IODispatcher
import com.riza.example.detail.data.usecase.GetMovieDetail
import com.riza.example.detail.data.usecase.GetMovieReviews
import com.riza.example.detail.data.usecase.GetMovieReviews.GetReviewResult
import com.riza.example.detail.data.usecase.GetMovieTrailers
import com.riza.example.detail.movie.state.MovieDetailItemModel
import com.riza.example.detail.movie.state.MovieDetailItemModel.Trailers.Success.Video
import com.riza.example.explore.navigator.GenreDetailIntentParam
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by ahmadriza on 21/07/23.
 */
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetail: GetMovieDetail,
    private val getMovieTrailers: GetMovieTrailers,
    private val getMovieReviews: GetMovieReviews,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<MovieDetailViewModel.Intent,
        MovieDetailViewModel.State,
        MovieDetailViewModel.Effect>(
    State()
) {
    data class State(
        val displayItems: List<MovieDetailItemModel> = emptyList()
    )

    sealed interface Intent {
        data class OnViewCreated(val movieId: Int) : Intent
        object LoadMoreReviews : Intent
        object RetryLoadMoreReviews : Intent
        object RetryGetDetail : Intent
        object RetryGetTrailer : Intent

        data class OnTrailerClick(val videoId: String): Intent

        data class OnGenreClick(val genre: MovieDetailItemModel.Detail.Success.Genre): Intent
    }


    sealed interface Effect {
        data class OpenYoutubePlayer(val videoId: String): Effect
        data class OpenGenreDetail(val intentParam: GenreDetailIntentParam): Effect
    }

    private var movieId: Int = 0
    private var nextPageParam: GetMovieReviews.Param? = null

    private val mutex = Mutex()

    override fun onIntentReceived(intent: Intent) {
        when (intent) {
            is Intent.OnViewCreated -> onViewCreated(intent.movieId)
            Intent.LoadMoreReviews -> onLoadMoreReviews()
            Intent.RetryGetDetail -> onRetryGetDetail()
            Intent.RetryGetTrailer -> onRetryGetTrailer()
            Intent.RetryLoadMoreReviews -> onRetryLoadMoreReviews()
            is Intent.OnTrailerClick -> {
                setEffect(Effect.OpenYoutubePlayer(intent.videoId))
            }

            is Intent.OnGenreClick -> onGenreClick(intent.genre)
        }
    }

    private fun onGenreClick(genre: MovieDetailItemModel.Detail.Success.Genre) {
        val intentParam = GenreDetailIntentParam(
            genre.id, genre.name
        )
        setEffect(Effect.OpenGenreDetail(intentParam))
    }

    private fun onRetryGetTrailer() {
        viewModelScope.launch {
            updateItem(
                viewState.displayItems.first { it is MovieDetailItemModel.Trailers },
                MovieDetailItemModel.Trailers.Loading
            )
            loadMovieTrailers()
        }
    }

    private fun onRetryGetDetail() {
        viewModelScope.launch {
            updateItem(
                viewState.displayItems.first { it is MovieDetailItemModel.Detail },
                MovieDetailItemModel.Detail.Loading
            )
            loadMovieDetail()
        }
    }



    private fun onRetryLoadMoreReviews() {
        viewModelScope.launch {
            updateItem(
                viewState.displayItems.first { it is MovieDetailItemModel.ErrorLoadMoreReview },
                MovieDetailItemModel.LoadMoreReview
            )
        }
    }

    private fun onLoadMoreReviews() {
        loadMovieReview()
    }

    private fun onViewCreated(movieId: Int) {
        this.movieId = movieId
        nextPageParam = GetMovieReviews.Param(
            movieId = movieId, page = 1
        )
        setState { copy(displayItems = getInitialLoadingTemplate()) }
        loadMovieDetail()
        loadMovieTrailers()
        loadMovieReview()
    }

    private fun loadMovieDetail() {
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { getMovieDetail(movieId) }) {
                GetMovieDetail.GetDetailResult.Error -> {
                    updateItem(
                        viewState.displayItems.first { it is MovieDetailItemModel.Detail },
                        MovieDetailItemModel.Detail.Error
                    )
                    updateItem(
                        viewState.displayItems.first { it is MovieDetailItemModel.Overview },
                        MovieDetailItemModel.Overview.Error
                    )
                    updateItem(
                        viewState.displayItems.first { it is MovieDetailItemModel.ReviewTitle },
                        MovieDetailItemModel.ReviewTitle.Error
                    )
                }

                is GetMovieDetail.GetDetailResult.Success -> {
                    val detail = result.detail.let {
                        MovieDetailItemModel.Detail.Success(
                            title = it.title,
                            thumbnail = it.thumbnail,
                            releaseDate = it.releaseDate,
                            countries = it.countries,
                            genres = it.genres.map { genre ->
                                MovieDetailItemModel.Detail.Success.Genre(
                                    name = genre.name, id = genre.id
                                )
                            }
                        )
                    }
                    updateItem(
                        viewState.displayItems.first { it is MovieDetailItemModel.Detail },
                        detail
                    )
                    updateItem(
                        viewState.displayItems.first { it is MovieDetailItemModel.Overview },
                        MovieDetailItemModel.Overview.Success(
                            overview = result.detail.overview
                        )
                    )
                    updateItem(
                        viewState.displayItems.first { it is MovieDetailItemModel.ReviewTitle },
                        MovieDetailItemModel.ReviewTitle.Success(
                            average = result.detail.voteAverage.toString(),
                            total = result.detail.voteCount.toString()
                        )
                    )
                }
            }
        }
    }

    private fun loadMovieTrailers() {
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { getMovieTrailers(movieId) }) {
                GetMovieTrailers.GetMovieTrailersResult.Empty -> {
                    updateItem(
                        viewState.displayItems.first { it is MovieDetailItemModel.Trailers },
                        null
                    )
                }

                GetMovieTrailers.GetMovieTrailersResult.Error -> {
                    updateItem(
                        viewState.displayItems.first { it is MovieDetailItemModel.Trailers },
                        MovieDetailItemModel.Trailers.Error
                    )
                }

                is GetMovieTrailers.GetMovieTrailersResult.Success -> {
                    updateItem(
                        viewState.displayItems.first { it is MovieDetailItemModel.Trailers },
                        MovieDetailItemModel.Trailers.Success(
                            trailers = result.trailers.map {
                                Video(
                                    thumbnail = it.thumbnail,
                                    youtubeKey = it.key,
                                    title = it.name,
                                    id = it.id
                                )
                            }
                        )
                    )
                }
            }
        }
    }

    private fun loadMovieReview() {
        viewModelScope.launch {
            val param = nextPageParam ?: return@launch
            when (val result = getMovieReviews(param)) {
                GetReviewResult.Empty -> {
                    val items = viewState.displayItems.filter {
                        it !is MovieDetailItemModel.LoadMoreReview
                    }
                    setState { copy(displayItems = items) }
                }

                GetReviewResult.Error -> {
                    val items = viewState.displayItems.filter {
                        it !is MovieDetailItemModel.LoadMoreReview
                    }.toMutableList()
                    items.add(MovieDetailItemModel.ErrorLoadMoreReview)
                    setState { copy(displayItems = items.toMutableList()) }
                }

                is GetReviewResult.Success -> {

                    val items = viewState.displayItems.filter {
                        it !is MovieDetailItemModel.LoadMoreReview
                    }.toMutableList()
                    val reviews = result.reviews.map {
                        MovieDetailItemModel.Review(
                            id = it.id,
                            username = it.username,
                            time = it.time,
                            review = it.rating,
                            description = it.content
                        )
                    }
                    items.addAll(reviews)
                    if (result.currentPage == result.totalPage) {
                        nextPageParam = null
                    } else {
                        nextPageParam = GetMovieReviews.Param(
                            movieId = movieId, page = result.currentPage + 1
                        )
                        items.add(MovieDetailItemModel.LoadMoreReview)
                    }
                    setState { copy(displayItems = items.toMutableList()) }
                }
            }
        }
    }

    private suspend fun updateItem(
        old: MovieDetailItemModel,
        new: MovieDetailItemModel?
    ) {
        mutex.withLock {
            val mutableItems = viewState.displayItems.toMutableList()
            val index = mutableItems.indexOf(old)
            if (index != -1) {
                if (new == null) {
                    mutableItems.removeAt(index)
                } else {
                    mutableItems[index] = new
                }
            }
            setState { copy(displayItems = mutableItems.toList()) }
        }
    }

    private fun getInitialLoadingTemplate(): List<MovieDetailItemModel> {
        return listOf(
            MovieDetailItemModel.Detail.Loading,
            MovieDetailItemModel.Trailers.Loading,
            MovieDetailItemModel.Overview.Loading,
            MovieDetailItemModel.ReviewTitle.Loading,
        )
    }

}