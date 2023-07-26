package com.riza.example.explore.data.usecase

import com.riza.example.common.base.BaseUseCase
import com.riza.example.common.date.DateFormat
import com.riza.example.common.date.DateFormatter
import com.riza.example.common.model.Result
import com.riza.example.explore.data.ExploreRepository
import com.riza.example.explore.data.entity.MovieListEntity
import com.riza.example.explore.data.model.MovieItem
import com.riza.example.explore.data.usecase.GetMoviesByGenre.GetMoviesByGenreResult
import com.riza.example.explore.di.ExploreServiceScope
import com.riza.example.network.mapper.toTmdbImageUrl
import javax.inject.Inject

/**
 * Created by ahmadriza on 19/07/23.
 */
@ExploreServiceScope
class GetMoviesByGenre @Inject constructor(
    private val repository: ExploreRepository,
    private val dateFormatter: DateFormatter
) : BaseUseCase<GetMoviesByGenreResult, GetMoviesByGenre.Params>() {

    data class Params(
        val page: Int,
        val genreId: Int
    )

    sealed interface GetMoviesByGenreResult {
        data class Success(
            val currentPage: Int,
            val totalPage: Int,
            val totalResult: Int,
            val movies: List<MovieItem>
        ) : GetMoviesByGenreResult

        object Empty : GetMoviesByGenreResult
        data class Error(val message: String) : GetMoviesByGenreResult
    }

    override suspend fun build(params: Params?): GetMoviesByGenreResult {
        requireNotNull(params)
        return when (
            val result = repository.getMoviesByGenre(
                genreId = params.genreId,
                page = params.page
            )
        ) {
            is Result.Success.WithData -> {
                if (result.data.results.isNullOrEmpty()) GetMoviesByGenreResult.Empty
                else result.data.toResult()
            }

            is Result.Success.Empty -> GetMoviesByGenreResult.Empty
            is Result.Error -> GetMoviesByGenreResult.Error(result.errorMessage)
        }
    }

    private fun MovieListEntity.toResult(): GetMoviesByGenreResult.Success {
        return GetMoviesByGenreResult.Success(
            currentPage = page ?: 0,
            totalPage = totalPages ?: 0,
            totalResult = totalResults ?: 0,
            movies = results.orEmpty().map {
                val formattedDate = dateFormatter.getFormattedDateOrEmpty(
                    date = it.releaseDate.orEmpty(),
                    originFormat = DateFormat.YEAR_MONTH_DAY_DASH,
                    targetFormat = DateFormat.MONTH_YEAR
                )
                MovieItem(
                    adult = it.adult ?: false,
                    backdropPath = it.backdropPath?.toTmdbImageUrl().orEmpty(),
                    genreIds = listOf(),
                    id = it.id ?: 0,
                    originalLanguage = it.originalLanguage.orEmpty(),
                    originalTitle = it.originalTitle.orEmpty(),
                    overview = it.overview.orEmpty(),
                    popularity = it.popularity ?: 0.0,
                    posterPath = it.posterPath?.toTmdbImageUrl().orEmpty(),
                    releaseDate = formattedDate,
                    title = it.title.orEmpty(),
                    video = it.video ?: false,
                    voteAverage = it.voteAverage ?: 0.0,
                    voteCount = it.voteCount ?: 0
                )
            }
        )
    }
}
