package com.riza.example.auth.di

import com.riza.example.common.di.CoreComponent
import dagger.Component


@AuthScope
@Component(
    modules = [AuthApiModule::class],
    dependencies = [
        CoreComponent::class
    ]
)
interface AuthApiComponentImpl : AuthApiComponent
