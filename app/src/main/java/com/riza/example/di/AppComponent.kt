package com.riza.example.di

import com.riza.example.common.di.CoreComponent
import com.riza.example.common.di.CoreComponentHolder
import com.riza.example.explore.di.ExploreApiComponent
import com.riza.example.explore.di.ExploreApiComponentHolder
import com.riza.example.main.MainActivity
import dagger.Component

@AppScope
@Component(
    dependencies = [
        CoreComponent::class,
        ExploreApiComponent::class
    ],
    modules = [AppViewModelModule::class]
)
interface AppComponent {
    fun inject(target: MainActivity)
}

fun buildAppComponent(): AppComponent {
    return DaggerAppComponent.builder()
        .coreComponent(CoreComponentHolder.coreComponent)
        .exploreApiComponent(ExploreApiComponentHolder.exploreApiComponent)
        .build()
}
