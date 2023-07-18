package com.riza.example.di

import com.riza.example.common.di.CoreComponent
import com.riza.example.common.di.CoreComponentHolder
import com.riza.example.detail.DetailActivity
import com.riza.example.home.MainActivity
import com.riza.example.service.di.GithubServiceComponent
import com.riza.example.service.di.GithubServiceComponentHolder
import dagger.Component

@AppScope
@Component(
    dependencies = [
        CoreComponent::class,
        GithubServiceComponent::class
    ],
    modules = [AppViewModelModule::class]
)
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: DetailActivity)
}

fun buildAppComponent(): AppComponent {
    return DaggerAppComponent.builder()
        .coreComponent(CoreComponentHolder.coreComponent)
        .githubServiceComponent(GithubServiceComponentHolder.githubServiceComponent)
        .build()
}
