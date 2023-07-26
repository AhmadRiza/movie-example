package com.riza.example.cache.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
class CacheModule(private val applicationContext: Context) {

    companion object {
        internal const val APP_DATABASE_NAME = "github.database"
        internal const val APP_PREFERENCES_KEY = "github.shared.preferences"
    }

    @Provides
    @CacheScope
    fun provideSharedPreferences(): SharedPreferences {
        return applicationContext.getSharedPreferences(
            APP_PREFERENCES_KEY,
            Context.MODE_PRIVATE
        )
    }
}
