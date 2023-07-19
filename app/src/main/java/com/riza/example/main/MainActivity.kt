package com.riza.example.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.riza.example.auth.navigator.AuthNavigator
import com.riza.example.di.buildAppComponent
import javax.inject.Inject

/**
 * Created by ahmadriza on 18/07/23.
 */
class MainActivity: AppCompatActivity() {

    @Inject
    lateinit var authNavigator: AuthNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        buildAppComponent().inject(this)

        // Keep the splash screen visible for this Activity
        splashScreen.setKeepOnScreenCondition { true }
        startActivity(authNavigator.getRegisterIntent(this))
        finish()
    }

}