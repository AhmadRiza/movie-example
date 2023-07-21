package com.riza.example.detail.data

import com.riza.example.common.model.Result
import com.riza.example.detail.data.entity.MovieDataEntity
import com.riza.example.detail.data.entity.MovieReviewDataEntity
import com.riza.example.detail.data.entity.MovieTrailerDataEntity
import com.riza.example.detail.data.model.MovieDetail

/**
 * Created by ahmadriza on 19/07/23.
 */
interface DetailRepository {
    suspend fun getMovieDetal(movieId: Int): Result<MovieDataEntity>

    suspend fun getMoviesReviews(
        movieId: Int,
        page: Int
    ): Result<MovieReviewDataEntity>

    suspend fun getMoviesVideos(
        movieId: Int
    ): Result<MovieTrailerDataEntity>

}