package com.riza.example.cache.di

import android.content.Context

object CacheComponentHolder {
    lateinit var cacheComponent: CacheComponent

    fun buildComponent(applicationContext: Context) {
        cacheComponent = DaggerCacheComponent.builder()
            .cacheModule(CacheModule(applicationContext = applicationContext))
            .build()
    }
}
