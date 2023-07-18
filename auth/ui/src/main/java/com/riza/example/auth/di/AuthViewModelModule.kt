package com.riza.example.auth.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riza.example.auth.register.RegisterViewModel
import com.riza.example.common.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    fun bindMainViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    fun bindAppViewModelFactory(
        viewModelFactory: AuthViewModelFactory
    ): ViewModelProvider.Factory
}
