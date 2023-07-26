package com.riza.example.detail.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riza.example.common.di.ViewModelKey
import com.riza.example.detail.movie.MovieDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface DetailViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    fun bindGenreDetailViewModel(viewModel: MovieDetailViewModel): ViewModel

    @Binds
    fun bindAppViewModelFactory(
        viewModelFactory: DetailViewModelFactory
    ): ViewModelProvider.Factory
}
