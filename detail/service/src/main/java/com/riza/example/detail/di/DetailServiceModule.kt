package com.riza.example.detail.di

import com.riza.example.detail.data.DetailRepository
import com.riza.example.detail.data.DetailRepositoryImpl
import com.riza.example.detail.data.remote.DetailService
import dagger.Module
import dagger.Provides
import javax.inject.Named
import retrofit2.Retrofit

@Module
class DetailServiceModule {

    @DetailServiceScope
    @Provides
    fun provideGithubRepository(
        exploreRepositoryImpl: DetailRepositoryImpl
    ): DetailRepository = exploreRepositoryImpl

    @DetailServiceScope
    @Provides
    fun provideGithubService(
        @Named("tmdb-retrofit") retrofit: Retrofit,
    ): DetailService {
        return retrofit.create(DetailService::class.java)
    }
}
