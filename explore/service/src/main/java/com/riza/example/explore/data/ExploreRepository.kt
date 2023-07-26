package com.riza.example.explore.data

import com.riza.example.common.model.Result
import com.riza.example.explore.data.entity.GenresEntity
import com.riza.example.explore.data.entity.MovieListEntity

/**
 * Created by ahmadriza on 19/07/23.
 */
interface ExploreRepository {
    suspend fun getGenres(): Result<List<GenresEntity.GenreEntity>>
    suspend fun getMoviesByGenre(
        genreId: Int,
        page: Int
    ): Result<MovieListEntity>
}
