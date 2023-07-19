package com.riza.example.explore.di

import androidx.annotation.VisibleForTesting
import com.riza.example.common.di.CoreComponentHolder
import com.riza.example.network.NetworkComponentHolder

object ExploreServiceComponentHolder {
    val exploreServiceComponent: ExploreServiceComponent by lazy {
        mockExploreServiceComponent?.let { return@lazy it }
        DaggerExploreServiceComponent.builder()
            .coreComponent(CoreComponentHolder.coreComponent)
            .networkComponent(NetworkComponentHolder.networkComponent)
            .build()
    }

    @get:VisibleForTesting
    @set:VisibleForTesting
    var mockExploreServiceComponent: ExploreServiceComponent? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }
}
