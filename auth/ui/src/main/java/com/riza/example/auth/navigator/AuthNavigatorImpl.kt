package com.riza.example.auth.navigator

import android.content.Context
import android.content.Intent
import com.riza.example.auth.register.RegisterActivity
import com.riza.example.auth.navigator.AuthNavigator
import javax.inject.Inject

/**
 * Created by ahmadriza on 18/07/23.
 */
class AuthNavigatorImpl @Inject constructor(): AuthNavigator {
    override fun getRegisterIntent(context: Context): Intent {
        return Intent(context, RegisterActivity::class.java)
    }
}