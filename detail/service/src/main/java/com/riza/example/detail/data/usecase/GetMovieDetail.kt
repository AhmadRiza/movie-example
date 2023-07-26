package com.riza.example.detail.data.usecase

import com.riza.example.common.base.BaseUseCase
import com.riza.example.common.date.DateFormat
import com.riza.example.common.date.DateFormatter
import com.riza.example.common.model.Result
import com.riza.example.detail.data.DetailRepository
import com.riza.example.detail.data.entity.MovieDataEntity
import com.riza.example.detail.data.model.MovieDetail
import com.riza.example.detail.di.DetailServiceScope
import com.riza.example.network.mapper.toTmdbImageUrl
import java.util.Date
import javax.inject.Inject

/**
 * Created by ahmadriza on 21/07/23.
 */
@DetailServiceScope
class GetMovieDetail @Inject constructor(
    private val repository: DetailRepository,
    private val dateFormatter: DateFormatter
) : BaseUseCase<GetMovieDetail.GetDetailResult, Int>() {

    sealed interface GetDetailResult {
        data class Success(val detail: MovieDetail) : GetDetailResult
        object Error : GetDetailResult
    }

    override suspend fun build(params: Int?): GetDetailResult {
        requireNotNull(params)
        return when (val result = repository.getMovieDetal(params)) {
            is Result.Error,
            is Result.Success.Empty -> GetDetailResult.Error
            is Result.Success.WithData -> GetDetailResult.Success(result.data.toMovieDetail())
        }
    }

    private fun MovieDataEntity.toMovieDetail(): MovieDetail {
        val releaseDate = dateFormatter.getDateOrNull(
            releaseDate.orEmpty(), DateFormat.YEAR_MONTH_DAY_DASH
        )
        val formattedDate = releaseDate?.let { date: Date ->
            dateFormatter.getFormattedDate(date, DateFormat.DATE_ONLY)
        }

        val countries = productionCountries.orEmpty().map { it.name }
            .joinToString(separator = ", ")

        return MovieDetail(
            title = title.orEmpty(),
            thumbnail = posterPath?.toTmdbImageUrl().orEmpty(),
            genres = genres.orEmpty().take(3).map {
                MovieDetail.Genre(
                    id = it.id ?: 0,
                    name = it.name.orEmpty()
                )
            },
            overview = overview.orEmpty(),
            releaseDate = formattedDate.orEmpty(),
            countries = countries,
            voteCount = voteCount ?: 0,
            voteAverage = voteAverage ?: 0.0

        )
    }
}
