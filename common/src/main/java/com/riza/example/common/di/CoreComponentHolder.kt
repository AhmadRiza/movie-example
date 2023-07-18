package com.riza.example.common.di

import android.content.Context
import android.content.SharedPreferences
import com.riza.example.common.router.NavigationRouter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object CoreComponentHolder {
    lateinit var coreComponent: CoreComponent

    fun buildComponent(
        applicationContext: Context,
        sharedPreferences: SharedPreferences,
        ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
        navigationRouter: NavigationRouter,
    ) {
        coreComponent = DaggerCoreComponent.builder()
            .coreModule(
                CoreModule(
                    sharedPreferences = sharedPreferences,
                    context = applicationContext,
                    ioDispatcher = ioDispatcher,
                    navigationRouter = navigationRouter,
                )
            )
            .build()
    }

    val isComponentInitialized: Boolean
        get() = this::coreComponent.isInitialized
}
