package com.riza.example.explore.data.remote

import com.riza.example.explore.data.entity.GenresEntity
import com.riza.example.explore.data.entity.MovieListEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ahmadriza on 19/07/23.
 */
interface ExploreService {

    @GET("genre/movie/list")
    suspend fun getGenres(): Response<GenresEntity>

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("page") page: Int,
        @Query("with_genres") genre: String,
    ): Response<MovieListEntity>
}
