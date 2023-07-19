package com.riza.example.explore.di

import com.riza.example.explore.genres.GenresActivity
import com.riza.example.common.di.CoreComponent
import com.riza.example.common.di.CoreComponentHolder
import dagger.Component

@ExploreScope
@Component(
    dependencies = [
        CoreComponent::class
    ],
    modules = [ExploreViewModelModule::class]
)
interface ExploreComponent {
    fun inject(target: GenresActivity)
}

fun buildAppComponent(): ExploreComponent {
    return DaggerExploreComponent.builder()
        .coreComponent(CoreComponentHolder.coreComponent)
        .build()
}
