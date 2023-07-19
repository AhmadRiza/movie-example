package com.riza.example.explore.di

import com.riza.example.common.di.CoreComponent
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

}
