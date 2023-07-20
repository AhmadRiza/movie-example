package com.riza.example.explore.navigator

import android.content.Context
import android.content.Intent
import com.riza.example.explore.genredetail.GenreDetailActivity
import com.riza.example.explore.genres.GenresActivity
import javax.inject.Inject

/**
 * Created by ahmadriza on 18/07/23.
 */
class ExploreNavigatorImpl @Inject constructor(): ExploreNavigator {
    override fun getGenresIntent(context: Context): Intent {
        return Intent(context, GenresActivity::class.java)
    }

    override fun getGenreDetailIntent(context: Context, param: GenreDetailIntentParam): Intent {
        return GenreDetailActivity.createIntent(context, param)
    }
}