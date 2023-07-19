package com.riza.example.di

import com.riza.example.auth.di.AuthApiComponent
import com.riza.example.auth.di.AuthApiComponentHolder
import com.riza.example.common.di.CoreComponent
import com.riza.example.common.di.CoreComponentHolder
import com.riza.example.main.MainActivity
import dagger.Component

@AppScope
@Component(
    dependencies = [
        CoreComponent::class,
        AuthApiComponent::class
    ],
    modules = [AppViewModelModule::class]
)
interface AppComponent {
    fun inject(target: MainActivity)
}

fun buildAppComponent(): AppComponent {
    return DaggerAppComponent.builder()
        .coreComponent(CoreComponentHolder.coreComponent)
        .authApiComponent(AuthApiComponentHolder.authApiComponent)
        .build()
}
