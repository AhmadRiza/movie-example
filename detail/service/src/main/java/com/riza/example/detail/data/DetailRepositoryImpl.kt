package com.riza.example.detail.data

import com.riza.example.common.entities.ErrorNetworkResult
import com.riza.example.common.entities.NetworkResult
import com.riza.example.common.model.Result
import com.riza.example.common.model.toEmptyResult
import com.riza.example.common.model.toErrorResult
import com.riza.example.detail.data.entity.MovieDataEntity
import com.riza.example.detail.data.entity.MovieReviewDataEntity
import com.riza.example.detail.data.entity.MovieTrailerDataEntity
import com.riza.example.detail.data.remote.DetailService
import com.riza.example.network.mapper.safeApiCall
import com.riza.example.network.mapper.toNetworkResult
import javax.inject.Inject

/**
 * Created by ahmadriza on 19/07/23.
 */
class DetailRepositoryImpl @Inject constructor(
    private val service: DetailService
) : DetailRepository {

    override suspend fun getMovieDetal(movieId: Int): Result<MovieDataEntity> {
        val result = safeApiCall {
            service.getMovieDetail(movieId).toNetworkResult()
        }
        return when (result) {
            is NetworkResult.Success.WithData -> Result.Success.WithData(
                result.value
            )
            is ErrorNetworkResult -> result.toErrorResult()
            is NetworkResult.Success.EmptyData -> result.toEmptyResult()
        }
    }

    override suspend fun getMoviesReviews(movieId: Int, page: Int): Result<MovieReviewDataEntity> {
        val result = safeApiCall {
            service.getMovieReviews(movieId = movieId, page = page).toNetworkResult()
        }
        return when (result) {
            is NetworkResult.Success.WithData -> Result.Success.WithData(
                result.value
            )
            is ErrorNetworkResult -> result.toErrorResult()
            is NetworkResult.Success.EmptyData -> result.toEmptyResult()
        }
    }

    override suspend fun getMoviesVideos(movieId: Int): Result<MovieTrailerDataEntity> {
        val result = safeApiCall {
            service.getMovieTrailers(movieId).toNetworkResult()
        }
        return when (result) {
            is NetworkResult.Success.WithData -> Result.Success.WithData(
                result.value
            )
            is ErrorNetworkResult -> result.toErrorResult()
            is NetworkResult.Success.EmptyData -> result.toEmptyResult()
        }
    }
}
