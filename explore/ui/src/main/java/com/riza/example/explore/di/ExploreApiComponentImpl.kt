package com.riza.example.explore.di

import com.riza.example.common.di.CoreComponent
import dagger.Component

@ExploreScope
@Component(
    modules = [ExploreApiModule::class],
    dependencies = [
        CoreComponent::class
    ]
)
interface ExploreApiComponentImpl : ExploreApiComponent
