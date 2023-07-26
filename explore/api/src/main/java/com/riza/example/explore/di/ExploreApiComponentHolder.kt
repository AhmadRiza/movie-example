package com.riza.example.explore.di

import androidx.annotation.VisibleForTesting
import com.riza.example.common.di.ComponentHolder

/**
 * Created by ahmadriza on 18/07/23.
 */
object ExploreApiComponentHolder {
    val exploreApiComponent: ExploreApiComponent by lazy {
        mockExploreApiComponent?.let { return@lazy it }

        ComponentHolder.getComponent(ExploreApiComponent::class.java)
            ?.createComponent() as ExploreApiComponent
    }

    @get:VisibleForTesting
    @set:VisibleForTesting
    var mockExploreApiComponent: ExploreApiComponent? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }
}
