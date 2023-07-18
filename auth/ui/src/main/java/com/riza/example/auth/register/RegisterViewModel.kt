package com.riza.example.auth.register

import com.riza.example.auth.register.compose.State
import com.riza.example.common.base.BaseViewModel

/**
 * Created by ahmadriza on 18/07/23.
 */
class RegisterViewModel: BaseViewModel<RegisterViewModel.Intent, State, RegisterViewModel.Effect>(
    State()
) {

    class Intent
    class Effect

    override fun onIntentReceived(intent: Intent) {

    }
}