package com.riza.example.detail.navigator

import android.content.Context
import android.content.Intent
import com.riza.example.detail.movie.MovieDetailActivity
import javax.inject.Inject

/**
 * Created by ahmadriza on 21/07/23.
 */
class DetailNavigatorImpl @Inject constructor() : DetailNavigator {
    override fun getMovieDetailIntent(context: Context, movieId: Int): Intent {
        return MovieDetailActivity.createIntent(context, movieId)
    }
}
