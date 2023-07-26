package com.riza.example.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
interface AppViewModelModule {

    @Binds
    fun bindAppViewModelFactory(
        viewModelFactory: AppViewModelFactory
    ): ViewModelProvider.Factory
}
