package com.riza.example.common.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.AssetManager
import com.riza.example.common.date.DateFormatter
import com.riza.example.common.date.DateFormatterImpl
import com.riza.example.common.locale.LocaleProvider
import com.riza.example.common.locale.LocaleProviderImpl
import com.riza.example.common.number.NumberFormatter
import com.riza.example.common.number.NumberFormatterImpl
import com.riza.example.common.router.NavigationRouter
import com.riza.example.common.time.TimeProvider
import com.riza.example.common.time.TimeProviderImpl
import com.riza.example.common.util.ResourceProvider
import com.riza.example.common.util.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher

@Module
class CoreModule(
    private val sharedPreferences: SharedPreferences,
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher,
    private val navigationRouter: NavigationRouter,
) {

    @Provides
    @Singleton
    fun provideAppContext(): Context = context

    @Provides
    fun provideAssetManager(): AssetManager {
        return context.assets
    }

    @Provides
    @Singleton
    fun provideResourceProvider(): ResourceProvider {
        return ResourceProviderImpl(context)
    }

    @Provides
    @Singleton
    @IODispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = ioDispatcher

    @Provides
    @Singleton
    fun provideDateFormatter(impl: DateFormatterImpl): DateFormatter = impl

    @Provides
    @Singleton
    fun provideTimeProvider(impl: TimeProviderImpl): TimeProvider = impl

    @Provides
    @Singleton
    fun provideLocaleProvider(impl: LocaleProviderImpl): LocaleProvider = impl

    @Provides
    @Singleton
    fun provideNumberFormatter(impl: NumberFormatterImpl): NumberFormatter = impl

    @Provides
    @Singleton
    fun provideNavigationRouter(): NavigationRouter = navigationRouter
}
