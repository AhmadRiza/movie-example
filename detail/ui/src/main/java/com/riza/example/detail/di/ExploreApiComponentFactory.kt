package com.riza.example.detail.di

import com.riza.example.common.di.ComponentFactory
import com.riza.example.common.di.CoreComponentHolder


class DetailApiComponentFactory : ComponentFactory<DetailApiComponent> {

    override fun createComponent(): DetailApiComponent {
        return DaggerDetailApiComponentImpl.builder()
            .coreComponent(CoreComponentHolder.coreComponent)
            .build()
    }
}
