package com.riza.example.detail.data.usecase

import com.riza.example.common.base.BaseUseCase
import com.riza.example.common.date.DateFormat
import com.riza.example.common.date.DateFormatter
import com.riza.example.common.model.Result
import com.riza.example.detail.data.DetailRepository
import com.riza.example.detail.data.entity.MovieDataEntity
import com.riza.example.detail.data.entity.TrailerEntity
import com.riza.example.detail.data.model.MovieDetail
import com.riza.example.detail.data.model.Trailer
import com.riza.example.detail.di.DetailServiceScope
import com.riza.example.network.mapper.toTmdbImageUrl
import com.riza.example.network.mapper.toYoutubeThumbnail
import java.util.Date
import javax.inject.Inject

/**
 * Created by ahmadriza on 21/07/23.
 */
@DetailServiceScope
class GetMovieTrailers @Inject constructor(
    private val repository: DetailRepository
) : BaseUseCase<GetMovieTrailers.GetMovieTrailersResult, Int>() {

    sealed interface GetMovieTrailersResult {
        object Error: GetMovieTrailersResult
        object Empty: GetMovieTrailersResult
        data class Success(val trailers: List<Trailer>): GetMovieTrailersResult
    }

    override suspend fun build(params: Int?): GetMovieTrailersResult {
        requireNotNull(params)
        return when (val result = repository.getMoviesVideos(params)) {
            is Result.Error -> GetMovieTrailersResult.Error
            is Result.Success.Empty -> GetMovieTrailersResult.Empty
            is Result.Success.WithData -> if (result.data.results.isNullOrEmpty()) {
                GetMovieTrailersResult.Empty
            } else {
                GetMovieTrailersResult.Success(
                    result.data.results!!
                        .filter { it.site.equals("youtube", true) }
                        .map { it.toTrailer() }
                )
            }
        }
    }

    private fun TrailerEntity.toTrailer(): Trailer {
        return Trailer(
            name = name.orEmpty(),
            thumbnail = key.orEmpty().toYoutubeThumbnail(),
            id = id.orEmpty(),
            key = key.orEmpty()
        )
    }
}