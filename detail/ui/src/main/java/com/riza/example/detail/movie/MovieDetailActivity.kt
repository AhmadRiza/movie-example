package com.riza.example.detail.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.riza.example.common.base.BaseVMComposeActivity
import com.riza.example.common.extension.parcelable
import com.riza.example.detail.di.buildComponent
import com.riza.example.detail.movie.compose.MovieDetailScreen

/**
 * Created by ahmadriza on 21/07/23.
 */
class MovieDetailActivity: BaseVMComposeActivity<MovieDetailViewModel.Intent,
        MovieDetailViewModel.State,
        MovieDetailViewModel.Effect,
        MovieDetailViewModel>() {

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun createIntent(context: Context, movieId: Int): Intent {
            return Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, movieId)
            }
        }
    }

    private val movieId: Int by lazy {
        intent.getIntExtra(EXTRA_MOVIE_ID, 0)
    }

    override fun inject() {
        buildComponent().inject(this)
    }

    override fun provideViewModel() = MovieDetailViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.observeAsState(MovieDetailViewModel.State())
            MovieDetailScreen(
                state = state,
                onBackPress = this::onBackPressed,
                sendIntent = this::dispatch
            )
        }
        dispatch(MovieDetailViewModel.Intent.OnViewCreated(movieId))
    }

    override fun renderEffect(effect: MovieDetailViewModel.Effect) {

    }

}