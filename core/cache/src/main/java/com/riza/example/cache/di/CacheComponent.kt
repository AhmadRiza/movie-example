package com.riza.example.cache.di

import android.content.SharedPreferences
import dagger.Component

@CacheScope
@Component(modules = [CacheModule::class])
interface CacheComponent {
    fun sharedPreferences(): SharedPreferences
}
