package com.riza.example.publiccomponent

import com.riza.example.common.di.ComponentHolder
import com.riza.example.explore.di.ExploreApiComponentFactory

object PublicComponentFactoryRegistry {

    fun registerPublicComponent() {
        ComponentHolder.register(ExploreApiComponentFactory())
    }
}
