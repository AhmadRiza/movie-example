package com.riza.example.explore.genres

import android.os.Bundle
import androidx.activity.compose.setContent
import com.riza.example.explore.di.buildAppComponent
import com.riza.example.explore.genres.compose.State
import com.riza.example.common.base.BaseVMComposeActivity
import com.riza.example.explore.genres.compose.GenresScreen

/**
 * Created by ahmadriza on 18/07/23.
 */
class GenresActivity: BaseVMComposeActivity<GenresViewModel.Intent, State, GenresViewModel.Effect, GenresViewModel>() {
    override fun inject() {
        buildAppComponent().inject(this)
    }

    override fun provideViewModel() = GenresViewModel::class.java


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GenresScreen()
        }
    }

    override fun renderEffect(effect: GenresViewModel.Effect) {

    }

}