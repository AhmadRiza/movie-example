package com.riza.example.auth.register

import android.os.Bundle
import androidx.activity.compose.setContent
import com.riza.example.auth.di.buildAppComponent
import com.riza.example.auth.register.compose.RegisterScreen
import com.riza.example.auth.register.compose.State
import com.riza.example.common.base.BaseVMComposeActivity

/**
 * Created by ahmadriza on 18/07/23.
 */
class RegisterActivity: BaseVMComposeActivity<RegisterViewModel.Intent, State, RegisterViewModel.Effect, RegisterViewModel>() {
    override fun inject() {
        buildAppComponent().inject(this)
    }

    override fun provideViewModel() = RegisterViewModel::class.java


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreen()
        }
    }

    override fun renderEffect(effect: RegisterViewModel.Effect) {

    }

}