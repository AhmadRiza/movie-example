package com.riza.example.common.di

import android.content.Context
import android.content.res.AssetManager
import com.riza.example.common.date.DateFormatter
import com.riza.example.common.locale.LocaleProvider
import com.riza.example.common.number.NumberFormatter
import com.riza.example.common.router.NavigationRouter
import com.riza.example.common.time.TimeProvider
import com.riza.example.common.util.ResourceProvider
import dagger.Component
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Singleton
@Component(
    modules = [
        CoreModule::class,
    ]
)
interface CoreComponent {

    fun context(): Context

    fun assetManager(): AssetManager

    @IODispatcher
    fun ioDispatcher(): CoroutineDispatcher

    fun resourceProvider(): ResourceProvider

    fun dateFormatter(): DateFormatter

    fun numberFormatter(): NumberFormatter

    fun timeProvider(): TimeProvider

    fun localeProvider(): LocaleProvider

    fun navigationRouter(): NavigationRouter
}
