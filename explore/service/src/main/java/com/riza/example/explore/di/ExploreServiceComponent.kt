package com.riza.example.explore.di

import com.riza.example.common.di.CoreComponent
import com.riza.example.explore.data.usecase.GetGenreEmoticon
import com.riza.example.explore.data.usecase.GetMovieGenres
import com.riza.example.explore.data.usecase.GetMoviesByGenre
import com.riza.example.network.NetworkComponent
import dagger.Component


@ExploreServiceScope
@Component(
    modules = [
        ExploreServiceModule::class
    ],
    dependencies = [
        CoreComponent::class,
        NetworkComponent::class
    ]
)
interface ExploreServiceComponent {
    fun getMovieGenres(): GetMovieGenres
    fun getMoviesByGenre(): GetMoviesByGenre
    fun getGenreEmoticon(): GetGenreEmoticon
}
