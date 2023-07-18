package com.riza.example.auth.di

import com.riza.example.auth.navigator.AuthNavigator
import com.riza.example.auth.navigator.AuthNavigatorImpl
import dagger.Binds
import dagger.Module


@Module
interface AuthApiModule {

    @Binds
    fun bindNavigator(impl: AuthNavigatorImpl): AuthNavigator
}
