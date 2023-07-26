package com.riza.example.explore.data

import com.riza.example.common.entities.ErrorNetworkResult
import com.riza.example.common.entities.NetworkResult
import com.riza.example.common.model.Result
import com.riza.example.common.model.toEmptyResult
import com.riza.example.common.model.toErrorResult
import com.riza.example.explore.data.entity.GenresEntity
import com.riza.example.explore.data.entity.MovieListEntity
import com.riza.example.explore.data.remote.ExploreService
import com.riza.example.network.mapper.safeApiCall
import com.riza.example.network.mapper.toNetworkResult
import javax.inject.Inject

/**
 * Created by ahmadriza on 19/07/23.
 */
class ExploreRepositoryImpl @Inject constructor(
    private val service: ExploreService
) : ExploreRepository {
    override suspend fun getGenres(): Result<List<GenresEntity.GenreEntity>> {
        val result = safeApiCall {
            service.getGenres().toNetworkResult()
        }
        return when (result) {
            is NetworkResult.Success.WithData -> Result.Success.WithData(
                result.value.genres.orEmpty()
            )
            is ErrorNetworkResult -> result.toErrorResult()
            is NetworkResult.Success.EmptyData -> result.toEmptyResult()
        }
    }

    override suspend fun getMoviesByGenre(genreId: Int, page: Int): Result<MovieListEntity> {
        val result = safeApiCall {
            service.getMoviesByGenre(page = page, genre = genreId.toString()).toNetworkResult()
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
