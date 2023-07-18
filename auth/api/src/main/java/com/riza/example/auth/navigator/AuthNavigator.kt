package com.riza.example.auth.navigator

import android.content.Context
import android.content.Intent

/**
 * Created by ahmadriza on 18/07/23.
 */
interface AuthNavigator {
    fun getRegisterIntent(context: Context): Intent
}