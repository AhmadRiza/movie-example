package com.riza.example.detail.data.remote

import com.riza.example.detail.data.entity.MovieDataEntity
import com.riza.example.detail.data.entity.MovieReviewDataEntity
import com.riza.example.detail.data.entity.MovieTrailerDataEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by ahmadriza on 19/07/23.
 */
interface DetailService {

    @GET("movie/{id}")
    suspend fun getMovieDetail(@Path("id") movieId: Int): Response<MovieDataEntity>

    @GET("movie/{id}/videos")
    suspend fun getMovieTrailers(@Path("id") movieId: Int): Response<MovieTrailerDataEntity>

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") movieId: Int,
        @Query("page") page: Int
    ): Response<MovieReviewDataEntity>
}
