package com.riza.example.explore.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riza.example.explore.genres.GenresViewModel
import com.riza.example.common.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ExploreViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GenresViewModel::class)
    fun bindMainViewModel(viewModel: GenresViewModel): ViewModel

    @Binds
    fun bindAppViewModelFactory(
        viewModelFactory: ExploreViewModelFactory
    ): ViewModelProvider.Factory
}
