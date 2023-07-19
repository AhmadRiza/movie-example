package com.riza.example.explore.genres

import com.riza.example.explore.genres.compose.State
import com.riza.example.common.base.BaseViewModel
import javax.inject.Inject

/**
 * Created by ahmadriza on 18/07/23.
 */
class GenresViewModel @Inject constructor(): BaseViewModel<GenresViewModel.Intent, State, GenresViewModel.Effect>(
    State()
) {

    class Intent
    class Effect

    override fun onIntentReceived(intent: Intent) {

    }
}