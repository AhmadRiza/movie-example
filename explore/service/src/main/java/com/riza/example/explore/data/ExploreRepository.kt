package com.riza.example.explore.data

import com.riza.example.common.model.Result
import com.riza.example.explore.data.model.Genre

/**
 * Created by ahmadriza on 19/07/23.
 */
interface ExploreRepository {
    suspend fun getGenres(): Result<List<Genre>>
}