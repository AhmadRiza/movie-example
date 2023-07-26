package com.riza.example.detail.di

import androidx.annotation.VisibleForTesting
import com.riza.example.common.di.ComponentHolder

/**
 * Created by ahmadriza on 18/07/23.
 */
object DetailApiComponentHolder {
    val detailApiComponent: DetailApiComponent by lazy {
        mockExploreApiComponent?.let { return@lazy it }

        ComponentHolder.getComponent(DetailApiComponent::class.java)
            ?.createComponent() as DetailApiComponent
    }

    @get:VisibleForTesting
    @set:VisibleForTesting
    var mockExploreApiComponent: DetailApiComponent? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }
}
