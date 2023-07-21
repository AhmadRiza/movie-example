package com.riza.example.detail.di

import com.riza.example.common.di.CoreComponent
import com.riza.example.common.di.CoreComponentHolder
import com.riza.example.detail.movie.MovieDetailActivity
import dagger.Component

@DetailScope
@Component(
    dependencies = [
        CoreComponent::class,
        DetailServiceComponent::class,
        DetailApiComponent::class
    ],
    modules = [DetailViewModelModule::class]
)
interface DetailComponent {
    fun inject(target: MovieDetailActivity)
}

fun buildComponent(): DetailComponent {
    return DaggerDetailComponent.builder()
        .coreComponent(CoreComponentHolder.coreComponent)
        .detailServiceComponent(DetailServiceComponentHolder.detailServiceComponent)
        .detailApiComponent(DetailApiComponentHolder.detailApiComponent)
        .build()
}
