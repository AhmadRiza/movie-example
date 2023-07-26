package com.riza.example.detail.navigator

import android.content.Context
import android.content.Intent

/**
 * Created by ahmadriza on 18/07/23.
 */
interface DetailNavigator {
    fun getMovieDetailIntent(context: Context, movieId: Int): Intent
}
