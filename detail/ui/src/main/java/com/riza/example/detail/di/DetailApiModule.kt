package com.riza.example.detail.di

import com.riza.example.detail.navigator.DetailNavigator
import com.riza.example.detail.navigator.DetailNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
interface DetailApiModule {

    @Binds
    fun bindNavigator(impl: DetailNavigatorImpl): DetailNavigator
}
