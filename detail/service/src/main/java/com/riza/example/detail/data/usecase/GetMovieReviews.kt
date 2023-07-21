package com.riza.example.detail.data.usecase

import com.riza.example.common.base.BaseUseCase
import com.riza.example.common.date.DateFormat
import com.riza.example.common.date.DateFormatter
import com.riza.example.common.model.Result
import com.riza.example.detail.data.DetailRepository
import com.riza.example.detail.data.entity.MovieDataEntity
import com.riza.example.detail.data.entity.ReviewEntity
import com.riza.example.detail.data.model.MovieDetail
import com.riza.example.detail.data.model.Review
import com.riza.example.detail.di.DetailServiceScope
import com.riza.example.network.mapper.toTmdbImageUrl
import java.util.Date
import javax.inject.Inject

/**
 * Created by ahmadriza on 21/07/23.
 */
@DetailServiceScope
class GetMovieReviews @Inject constructor(
    private val repository: DetailRepository,
    private val dateFormatter: DateFormatter
) : BaseUseCase<GetMovieReviews.GetReviewResult, GetMovieReviews.Param>() {

    data class Param(
        val movieId: Int,
        val page: Int
    )

    sealed interface GetReviewResult {
        data class Success(
            val currentPage: Int,
            val totalPage: Int,
            val reviews: List<Review>
        ): GetReviewResult

        object Error : GetReviewResult
        object Empty : GetReviewResult
    }

    override suspend fun build(params: Param?): GetReviewResult {
        requireNotNull(params)
        return when (
            val result = repository.getMoviesReviews(
                movieId = params.movieId,
                page = params.page
            )
        ) {
            is Result.Error -> GetReviewResult.Error
            is Result.Success.Empty -> GetReviewResult.Empty
            is Result.Success.WithData -> {
                if (result.data.results.orEmpty().isEmpty()) {
                    GetReviewResult.Empty
                } else {
                    GetReviewResult.Success(
                        currentPage = result.data.page?:0,
                        totalPage = result.data.totalPages?:0,
                        reviews = result.data.results?.map {it.toReview()}.orEmpty()
                    )
                }
            }
        }
    }

    private fun ReviewEntity.toReview() : Review {
        val reviewDate = dateFormatter.getDateOrNull(createdAt.orEmpty(), DateFormat.ISO_TIMESTAMP)
        val formattedDate = reviewDate?.let { date: Date ->
            dateFormatter.getFormattedDate(date.time, DateFormat.DATE_TIME_2)
        }
        return Review(
            id = id.orEmpty(),
            username = "@${authorDetails?.username.orEmpty()}",
            time = formattedDate.orEmpty(),
            rating = authorDetails?.rating?:0,
            content = content.orEmpty()
        )
    }
}