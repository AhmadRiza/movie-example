package com.riza.example.network

import android.content.Context

object NetworkComponentHolder {
    lateinit var networkComponent: NetworkComponent

    // Only Run in in the application level
    fun buildComponent(
        applicationContext: Context,
        githubToken: String
    ) {
        networkComponent = DaggerNetworkComponent.builder()
            .networkModule(
                NetworkModule(
                    context = applicationContext,
                    githubToken = githubToken
                )
            )
            .build()
    }
}
