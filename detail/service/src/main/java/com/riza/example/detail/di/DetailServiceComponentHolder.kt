package com.riza.example.detail.di

import androidx.annotation.VisibleForTesting
import com.riza.example.common.di.CoreComponentHolder
import com.riza.example.explore.di.ExploreServiceComponentHolder
import com.riza.example.network.NetworkComponentHolder

object DetailServiceComponentHolder {
    val detailServiceComponent: DetailServiceComponent by lazy {
        mockDetailServiceComponent?.let { return@lazy it }
        DaggerDetailServiceComponent.builder()
            .coreComponent(CoreComponentHolder.coreComponent)
            .networkComponent(NetworkComponentHolder.networkComponent)
            .build()
    }

    @get:VisibleForTesting
    @set:VisibleForTesting
    var mockDetailServiceComponent: DetailServiceComponent? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }
}
