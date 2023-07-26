package com.riza.example.explore.di

import com.riza.example.common.di.ComponentFactory
import com.riza.example.common.di.CoreComponentHolder

class ExploreApiComponentFactory : ComponentFactory<ExploreApiComponent> {

    override fun createComponent(): ExploreApiComponent {
        return DaggerExploreApiComponentImpl.builder()
            .coreComponent(CoreComponentHolder.coreComponent)
            .build()
    }
}
