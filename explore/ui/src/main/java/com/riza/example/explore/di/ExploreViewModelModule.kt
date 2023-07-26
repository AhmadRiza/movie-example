package com.riza.example.explore.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riza.example.common.di.ViewModelKey
import com.riza.example.explore.genredetail.GenreDetailViewModel
import com.riza.example.explore.genres.GenresViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ExploreViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GenresViewModel::class)
    fun bindGenresViewModel(viewModel: GenresViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(GenreDetailViewModel::class)
    fun bindGenreDetailViewModel(viewModel: GenreDetailViewModel): ViewModel

    @Binds
    fun bindAppViewModelFactory(
        viewModelFactory: ExploreViewModelFactory
    ): ViewModelProvider.Factory
}
