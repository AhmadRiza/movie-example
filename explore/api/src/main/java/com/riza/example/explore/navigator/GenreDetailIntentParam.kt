package com.riza.example.explore.navigator

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by ahmadriza on 20/07/23.
 */
@Parcelize
data class GenreDetailIntentParam(
    val genreId: Int,
    val genreName: String
) : Parcelable
