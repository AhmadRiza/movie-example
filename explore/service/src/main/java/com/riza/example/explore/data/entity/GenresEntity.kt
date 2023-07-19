package com.riza.example.explore.data.entity

import androidx.annotation.Keep

/**
 * Created by ahmadriza on 19/07/23.
 */
@Keep
data class GenresEntity(
    val genres: List<GenreEntity>?
) {
    @Keep
    data class GenreEntity(val id: Int?, val name: String?)
}