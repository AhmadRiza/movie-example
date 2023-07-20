package com.riza.example.network

import dagger.Component
import javax.inject.Named
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@NetworkScope
@Component(modules = [NetworkModule::class])
interface NetworkComponent {

    fun okHttpClient(): OkHttpClient

    @Named("tmdb-retrofit")
    fun tmdbRetrofit(): Retrofit
}
