package com.riza.example.auth.di

import com.riza.example.common.di.ComponentFactory
import com.riza.example.common.di.CoreComponentHolder


class AuthApiComponentFactory : ComponentFactory<AuthApiComponent> {

    override fun createComponent(): AuthApiComponent {
        return DaggerAuthApiComponentImpl.builder()
            .coreComponent(CoreComponentHolder.coreComponent)
            .build()
    }
}
