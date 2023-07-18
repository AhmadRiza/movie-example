package com.riza.example.auth.di

import androidx.annotation.VisibleForTesting
import com.riza.example.common.di.ComponentHolder

/**
 * Created by ahmadriza on 18/07/23.
 */
object AuthApiComponentHolder {
    val authApiComponent: AuthApiComponent by lazy {
        mockAuthApiComponent?.let { return@lazy it }

        ComponentHolder.getComponent(AuthApiComponent::class.java)
            ?.createComponent() as AuthApiComponent
    }

    @get:VisibleForTesting
    @set:VisibleForTesting
    var mockAuthApiComponent: AuthApiComponent? = null
        set(value) {
            if (field == null) {
                field = value
            }
        }
}