package com.riza.example.explore.navigator

import android.content.Context
import android.content.Intent

/**
 * Created by ahmadriza on 18/07/23.
 */
interface ExploreNavigator {
    fun getGenresIntent(context: Context): Intent
    fun getGenreDetailIntent(context: Context, param: GenreDetailIntentParam): Intent
}
