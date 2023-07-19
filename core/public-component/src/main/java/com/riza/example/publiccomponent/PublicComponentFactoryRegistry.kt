package com.riza.example.publiccomponent

import com.riza.example.auth.di.AuthApiComponentFactory
import com.riza.example.common.di.ComponentHolder

object PublicComponentFactoryRegistry {

    fun registerPublicComponent() {
        ComponentHolder.register(AuthApiComponentFactory())
    }
}
