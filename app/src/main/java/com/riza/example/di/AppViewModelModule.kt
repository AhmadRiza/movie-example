package com.riza.example.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AppViewModelModule {

    @Binds
    fun bindAppViewModelFactory(
        viewModelFactory: AppViewModelFactory
    ): ViewModelProvider.Factory
}
