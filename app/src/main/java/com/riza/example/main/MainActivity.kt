package com.riza.example.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.riza.example.di.buildAppComponent
import com.riza.example.explore.navigator.ExploreNavigator
import javax.inject.Inject

/**
 * Created by ahmadriza on 18/07/23.
 */
class MainActivity: AppCompatActivity() {

    @Inject
    lateinit var exploreNavigator: ExploreNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        buildAppComponent().inject(this)

        // Keep the splash screen visible for this Activity
        splashScreen.setKeepOnScreenCondition { true }
        startActivity(exploreNavigator.getGenresIntent(this))
        finish()
    }

}