package com.riza.example.detail.di

import com.riza.example.common.di.CoreComponent
import com.riza.example.detail.data.usecase.GetMovieDetail
import com.riza.example.detail.data.usecase.GetMovieReviews
import com.riza.example.detail.data.usecase.GetMovieTrailers
import com.riza.example.network.NetworkComponent
import dagger.Component

@DetailServiceScope
@Component(
    modules = [
        DetailServiceModule::class
    ],
    dependencies = [
        CoreComponent::class,
        NetworkComponent::class
    ]
)
interface DetailServiceComponent {
    fun getMovieDetail(): GetMovieDetail
    fun getMovieReviews(): GetMovieReviews
    fun getMovieTrailers(): GetMovieTrailers
}
