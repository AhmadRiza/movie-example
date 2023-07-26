package com.riza.example.explore.data.usecase

import com.riza.example.common.base.BaseUseCase
import com.riza.example.common.model.Result
import com.riza.example.explore.data.ExploreRepository
import com.riza.example.explore.data.model.Genre
import com.riza.example.explore.data.usecase.GetMovieGenres.GetGenresResult
import com.riza.example.explore.di.ExploreServiceScope
import javax.inject.Inject

/**
 * Created by ahmadriza on 19/07/23.
 */

@ExploreServiceScope
class GetMovieGenres @Inject constructor(
    private val repository: ExploreRepository,
    private val getGenreEmoticon: GetGenreEmoticon
) : BaseUseCase<GetGenresResult, Unit>() {

    sealed interface GetGenresResult {
        object Empty : GetGenresResult
        data class Error(val message: String) : GetGenresResult
        data class Success(val genres: List<Genre>) : GetGenresResult
    }

    override suspend fun build(params: Unit?): GetGenresResult {
        return when (val result = repository.getGenres()) {
            is Result.Success.WithData -> {
                val genres = result.data.map {
                    Genre(
                        id = it.id ?: 0,
                        name = it.name.orEmpty(),
                        emoticon = getGenreEmoticon(it.name)
                    )
                }
                GetGenresResult.Success(genres)
            }

            is Result.Success.Empty -> GetGenresResult.Empty
            is Result.Error -> GetGenresResult.Error(result.errorMessage)
        }
    }
}
