package com.riza.example.explore.di

import com.riza.example.explore.data.ExploreRepository
import com.riza.example.explore.data.ExploreRepositoryImpl
import com.riza.example.explore.data.remote.ExploreService
import dagger.Module
import dagger.Provides
import javax.inject.Named
import retrofit2.Retrofit

@Module
class ExploreServiceModule {

    @ExploreServiceScope
    @Provides
    fun provideGithubRepository(
        exploreRepositoryImpl: ExploreRepositoryImpl
    ): ExploreRepository = exploreRepositoryImpl

    @ExploreServiceScope
    @Provides
    fun provideGithubService(
        @Named("tmdb-retrofit") retrofit: Retrofit,
    ): ExploreService {
        return retrofit.create(ExploreService::class.java)
    }
}
