package com.riza.example

import android.app.Application
import com.riza.example.router.NavigationRouterImpl
import com.riza.example.cache.di.CacheComponentHolder
import com.riza.example.common.di.CoreComponentHolder
import com.riza.example.network.NetworkComponentHolder
import com.riza.example.publiccomponent.PublicComponentFactoryRegistry

/**
 * Created by ahmadriza on 15/08/22.
 * Copyright (c) 2022 Kitabisa. All rights reserved.
 */
class MyApp : Application() {

    override fun onCreate() {
        configureCoreComponents()
        super.onCreate()
    }

    private fun configureCoreComponents() {
        CacheComponentHolder.buildComponent(this)
        CoreComponentHolder.buildComponent(
            applicationContext = this,
            sharedPreferences = CacheComponentHolder.cacheComponent.sharedPreferences(),
            navigationRouter = NavigationRouterImpl()
        )
        NetworkComponentHolder.buildComponent(
            applicationContext = this,
            tmdbApiKey = BuildConfig.TMDB_API_KEY
        )
        PublicComponentFactoryRegistry.registerPublicComponent()
    }
}
