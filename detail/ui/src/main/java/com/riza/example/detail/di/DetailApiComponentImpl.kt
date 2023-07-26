package com.riza.example.detail.di

import com.riza.example.common.di.CoreComponent
import dagger.Component

@DetailScope
@Component(
    modules = [DetailApiModule::class],
    dependencies = [
        CoreComponent::class
    ]
)
interface DetailApiComponentImpl : DetailApiComponent
