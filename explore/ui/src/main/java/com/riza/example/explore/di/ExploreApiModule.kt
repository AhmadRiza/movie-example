package com.riza.example.explore.di

import com.riza.example.explore.navigator.ExploreNavigator
import com.riza.example.explore.navigator.ExploreNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
interface ExploreApiModule {

    @Binds
    fun bindNavigator(impl: ExploreNavigatorImpl): ExploreNavigator
}
