package com.riza.example.explore.data.remote

import com.riza.example.explore.data.entity.GenresEntity
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by ahmadriza on 19/07/23.
 */
interface ExploreService {

    @GET("genre/movie/list")
    suspend fun getGenres(): Response<GenresEntity>

}