package com.riza.example.explore.genredetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.riza.example.common.base.BaseVMComposeActivity
import com.riza.example.common.extension.parcelable
import com.riza.example.explore.di.buildAppComponent
import com.riza.example.explore.genredetail.compose.GenreDetailScreen
import com.riza.example.explore.navigator.GenreDetailIntentParam

/**
 * Created by ahmadriza on 20/07/23.
 */
class GenreDetailActivity : BaseVMComposeActivity<GenreDetailViewModel.Intent,
GenreDetailViewModel.State,
GenreDetailViewModel.Effect,
GenreDetailViewModel>() {

    companion object {
        private const val EXTRA_GENRE = "EXTRA_GENRE"

        fun createIntent(context: Context, genre: GenreDetailIntentParam): Intent {
            return Intent(context, GenreDetailActivity::class.java).apply {
                putExtra(EXTRA_GENRE, genre)
            }
        }
    }

    private val intentParam: GenreDetailIntentParam by lazy {
        intent.parcelable(EXTRA_GENRE)!!
    }
    
    override fun inject() {
        buildAppComponent().inject(this)
    }

    override fun provideViewModel() = GenreDetailViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.observeAsState(GenreDetailViewModel.State())
            GenreDetailScreen(
                state = state,
                onBackPress = this::onBackPressed,
                sendIntent = this::dispatch
            )
        }
        dispatch(GenreDetailViewModel.Intent.OnViewCreated(intentParam))
    }

    override fun renderEffect(effect: GenreDetailViewModel.Effect) {

    }

}