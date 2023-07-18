package com.riza.example

import android.app.Application
import com.riza.example.router.NavigationRouterImpl
import com.riza.example.cache.di.CacheComponentHolder
import com.riza.example.common.di.CoreComponentHolder
import com.riza.example.network.NetworkComponentHolder

/**
 * Created by ahmadriza on 15/08/22.
 * Copyright (c) 2022 Kitabisa. All rights reserved.
 */
class GithubCatalogApp : Application() {

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
            githubToken = "token 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"
        )
    }
}
