package com.riza.example.detail.di

import com.riza.example.common.di.CoreComponent
import com.riza.example.detail.data.usecase.GetMovieDetail
import com.riza.example.explore.di.ExploreServiceComponent
import com.riza.example.network.NetworkComponent
import dagger.Component


@DetailServiceScope
@Component(
    modules = [
        DetailServiceModule::class
    ],
    dependencies = [
        CoreComponent::class,
        NetworkComponent::class,
        ExploreServiceComponent::class
    ]
)
interface DetailServiceComponent {
    fun getMovieDetail(): GetMovieDetail
}
