package com.riza.example.auth.di

import com.riza.example.auth.register.RegisterActivity
import com.riza.example.common.di.CoreComponent
import com.riza.example.common.di.CoreComponentHolder
import dagger.Component

@AuthScope
@Component(
    dependencies = [
        CoreComponent::class
    ],
    modules = [AuthViewModelModule::class]
)
interface AuthComponent {
    fun inject(target: RegisterActivity)
}

fun buildAppComponent(): AuthComponent {
    return DaggerAuthComponent.builder()
        .coreComponent(CoreComponentHolder.coreComponent)
        .build()
}
