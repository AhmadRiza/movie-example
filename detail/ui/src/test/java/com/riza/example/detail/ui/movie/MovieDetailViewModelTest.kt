package com.riza.example.detail.ui.movie

import com.riza.example.detail.data.model.MovieDetail
import com.riza.example.detail.data.model.Review
import com.riza.example.detail.data.model.Trailer
import com.riza.example.detail.data.usecase.GetMovieDetail
import com.riza.example.detail.data.usecase.GetMovieDetail.GetDetailResult
import com.riza.example.detail.data.usecase.GetMovieReviews
import com.riza.example.detail.data.usecase.GetMovieReviews.GetReviewResult
import com.riza.example.detail.data.usecase.GetMovieTrailers
import com.riza.example.detail.data.usecase.GetMovieTrailers.GetMovieTrailersResult
import com.riza.example.detail.movie.MovieDetailViewModel
import com.riza.example.detail.movie.MovieDetailViewModel.Effect
import com.riza.example.detail.movie.MovieDetailViewModel.Intent
import com.riza.example.detail.movie.MovieDetailViewModel.State
import com.riza.example.detail.movie.state.MovieDetailItemModel
import com.riza.example.detail.movie.state.MovieDetailItemModel.Detail
import com.riza.example.detail.movie.state.MovieDetailItemModel.Trailers.Success.Video
import com.riza.example.explore.navigator.GenreDetailIntentParam
import com.riza.example.test.ViewModelBehaviourSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * Created by ahmadriza on 26/07/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailViewModelTest :
    ViewModelBehaviourSpec<Intent, State, Effect, MovieDetailViewModel>() {

    private val getMovieDetail = mockk<GetMovieDetail>()
    private val getMovieTrailers = mockk<GetMovieTrailers>()
    private val getMovieReviews = mockk<GetMovieReviews>()

    override val viewModel = MovieDetailViewModel(
        getMovieDetail = getMovieDetail,
        getMovieTrailers = getMovieTrailers,
        getMovieReviews = getMovieReviews,
        ioDispatcher = UnconfinedTestDispatcher(),
    )

    init {

        Given("MovieDetailViewModelTest") {

            setupBefore { }
            setupAfter {
                clearAllMocks()
            }

            When("On View Created") {
                And("Success Get Detail, Reviews, and Trailers") {
                    beforeTest {
                        mockGetDetailSuccess()
                        mockGetTrailersSuccess()
                        mockGetReviewsSuccess()
                    }
                    Then("Should display correct items") {
                        viewModel.onIntentReceived(Intent.OnViewCreated(mockMovieId))
                        observedStateList.first().displayItems shouldBe mockInitialLoadingDisplay
                        observedStateList.last().displayItems shouldBe listOf(
                            Detail.Success(
                                title = "Barbie",
                                thumbnail = "thumb.jpg",
                                releaseDate = "1 January 2023",
                                countries = "US",
                                genres = listOf(
                                    Detail.Success.Genre("Comedy", 1)
                                )
                            ),
                            MovieDetailItemModel.Trailers.Success(
                                listOf(
                                    Video(
                                        thumbnail = "thumb.jpg",
                                        youtubeKey = "k",
                                        title = "trailer",
                                        id = "1"
                                    )
                                )
                            ),
                            MovieDetailItemModel.Overview.Success(
                                overview = "Lorem"
                            ),
                            MovieDetailItemModel.ReviewTitle.Success(average = "8.0", total = "9"),
                            MovieDetailItemModel.Review(
                                id = "1",
                                username = "joni",
                                time = "2 days ago",
                                review = 9,
                                description = "Almost perfect"
                            ),
                            MovieDetailItemModel.LoadMoreReview
                        )
                    }
                }
                And("Fail Get Detail, Reviews, or Trailers") {
                    beforeTest {
                        coEvery { getMovieDetail(mockMovieId) } returns GetDetailResult.Error
                        coEvery {
                            getMovieTrailers(mockMovieId)
                        } returns GetMovieTrailersResult.Error
                        coEvery {
                            getMovieReviews(GetMovieReviews.Param(mockMovieId, 1))
                        } returns GetReviewResult.Error
                    }
                    Then("Should display correct items") {
                        viewModel.onIntentReceived(Intent.OnViewCreated(mockMovieId))
                        observedStateList.last().displayItems shouldBe listOf(
                            Detail.Error,
                            MovieDetailItemModel.Trailers.Error,
                            MovieDetailItemModel.Overview.Error,
                            MovieDetailItemModel.ReviewTitle.Error,
                            MovieDetailItemModel.ErrorLoadMoreReview,
                        )
                    }
                }
            }
            When("Load More Reviews") {
                And("Success got reviews") {
                    beforeTest {
                        mockGetDetailSuccess()
                        mockGetTrailersSuccess()
                        mockGetReviewsSuccess()
                        mockGetReviewsSuccess(2)
                    }
                    Then("should append the reviews") {
                        viewModel.onIntentReceived(Intent.OnViewCreated(mockMovieId))
                        viewModel.onIntentReceived(Intent.LoadMoreReviews)
                        observedStateList.last().displayItems shouldContainAll listOf(
                            MovieDetailItemModel.Review(
                                id = "2",
                                username = "joni",
                                time = "2 days ago",
                                review = 9,
                                description = "Almost perfect"
                            ),
                            MovieDetailItemModel.LoadMoreReview
                        )
                    }
                }

                And("Fail got reviews") {
                    beforeTest {
                        mockGetDetailSuccess()
                        mockGetTrailersSuccess()
                        mockGetReviewsSuccess()
                        coEvery {
                            getMovieReviews(GetMovieReviews.Param(mockMovieId, 2))
                        } returns GetReviewResult.Error
                    }
                    Then("should append the reviews") {
                        viewModel.onIntentReceived(Intent.OnViewCreated(mockMovieId))
                        viewModel.onIntentReceived(Intent.LoadMoreReviews)
                        observedStateList.last()
                            .displayItems shouldContain MovieDetailItemModel.ErrorLoadMoreReview
                    }
                }
            }

            When("Retry Get Detail") {
                beforeTest {
                    coEvery { getMovieDetail(mockMovieId) } returns GetDetailResult.Error
                    coEvery {
                        getMovieTrailers(mockMovieId)
                    } returns GetMovieTrailersResult.Error
                    coEvery {
                        getMovieReviews(GetMovieReviews.Param(mockMovieId, 1))
                    } returns GetReviewResult.Error
                }
                Then("Should invoke GetMovieDetail") {
                    viewModel.onIntentReceived(Intent.OnViewCreated(mockMovieId))
                    viewModel.onIntentReceived(Intent.RetryGetDetail)
                    coVerify(exactly = 2) {
                        getMovieDetail(mockMovieId)
                    }
                }
            }

            When("Retry Get Trailers") {
                beforeTest {
                    coEvery { getMovieDetail(mockMovieId) } returns GetDetailResult.Error
                    coEvery {
                        getMovieTrailers(mockMovieId)
                    } returns GetMovieTrailersResult.Error
                    coEvery {
                        getMovieReviews(GetMovieReviews.Param(mockMovieId, 1))
                    } returns GetReviewResult.Error
                }
                Then("Should invoke GetMovieTrailer") {
                    viewModel.onIntentReceived(Intent.OnViewCreated(mockMovieId))
                    viewModel.onIntentReceived(Intent.RetryGetTrailer)
                    coVerify(exactly = 2) {
                        getMovieTrailers(mockMovieId)
                    }
                }
            }

            When("Retry Load More Review") {
                beforeTest {
                    coEvery { getMovieDetail(mockMovieId) } returns GetDetailResult.Error
                    coEvery {
                        getMovieTrailers(mockMovieId)
                    } returns GetMovieTrailersResult.Error
                    coEvery {
                        getMovieReviews(GetMovieReviews.Param(mockMovieId, 1))
                    } returns GetReviewResult.Error
                }
                Then("Should invoke GetMovieReview") {
                    viewModel.onIntentReceived(Intent.OnViewCreated(mockMovieId))
                    viewModel.onIntentReceived(Intent.RetryLoadMoreReviews)
                    coVerify(exactly = 2) {
                        getMovieReviews(GetMovieReviews.Param(mockMovieId, 1))
                    }
                }
            }

            When("On Trailer Click") {
                Then("Should Open Youtube Player") {
                    viewModel.onIntentReceived(Intent.OnTrailerClick(videoId = "abc"))
                    observedEffectList.last() shouldBe Effect.OpenYoutubePlayer("abc")
                }
            }
            When("On Genre Click") {
                Then("Should Open Genre Detail Page") {
                    viewModel.onIntentReceived(
                        Intent.OnGenreClick(Detail.Success.Genre("comedy", 1))
                    )
                    observedEffectList.last() shouldBe Effect.OpenGenreDetail(
                        GenreDetailIntentParam(1, "comedy")
                    )
                }
            }
        }
    }

    private fun mockGetTrailersSuccess() {
        coEvery {
            getMovieTrailers(mockMovieId)
        } returns GetMovieTrailersResult.Success(
            trailers = listOf(
                Trailer(
                    name = "trailer", thumbnail = "thumb.jpg", id = "1", key = "k"
                )
            )
        )
    }

    private fun mockGetReviewsSuccess(page: Int = 1) {
        coEvery {
            getMovieReviews(GetMovieReviews.Param(mockMovieId, page))
        } returns GetReviewResult.Success(
            currentPage = 1, totalPage = 2,
            reviews = listOf(
                Review(
                    id = "$page",
                    username = "joni",
                    time = "2 days ago",
                    rating = 9,
                    content = "Almost perfect"
                )
            )
        )
    }

    private fun mockGetDetailSuccess() {
        coEvery {
            getMovieDetail(mockMovieId)
        } returns GetDetailResult.Success(
            detail = MovieDetail(
                title = "Barbie",
                thumbnail = "thumb.jpg",
                genres = listOf(
                    MovieDetail.Genre(1, "Comedy")
                ),
                overview = "Lorem",
                releaseDate = "1 January 2023",
                countries = "US",
                voteCount = 9,
                voteAverage = 8.0
            )
        )
    }

    private val mockMovieId = 10

    private val mockInitialLoadingDisplay
        get() = listOf(
            Detail.Loading,
            MovieDetailItemModel.Trailers.Loading,
            MovieDetailItemModel.Overview.Loading,
            MovieDetailItemModel.ReviewTitle.Loading,
        )
}
