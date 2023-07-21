package com.riza.example.explore.di

import com.riza.example.explore.genres.GenresActivity
import com.riza.example.common.di.CoreComponent
import com.riza.example.common.di.CoreComponentHolder
import com.riza.example.detail.di.DetailApiComponent
import com.riza.example.detail.di.DetailApiComponentHolder
import com.riza.example.explore.genredetail.GenreDetailActivity
import dagger.Component

@ExploreScope
@Component(
    dependencies = [
        CoreComponent::class,
        ExploreServiceComponent::class,
        ExploreApiComponent::class,
        DetailApiComponent::class,
    ],
    modules = [ExploreViewModelModule::class]
)
interface ExploreComponent {
    fun inject(target: GenresActivity)
    fun inject(target: GenreDetailActivity)
}

fun buildAppComponent(): ExploreComponent {
    return DaggerExploreComponent.builder()
        .coreComponent(CoreComponentHolder.coreComponent)
        .exploreServiceComponent(ExploreServiceComponentHolder.exploreServiceComponent)
        .exploreApiComponent(ExploreApiComponentHolder.exploreApiComponent)
        .detailApiComponent(DetailApiComponentHolder.detailApiComponent)
        .build()
}
