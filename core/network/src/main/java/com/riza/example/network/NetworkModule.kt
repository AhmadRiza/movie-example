package com.riza.example.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.riza.example.network.interceptor.HeaderInterceptor
import com.riza.example.network.interceptor.InternetConnectionInterceptor
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import javax.inject.Named
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule(
    private val context: Context,
    private val tmdbApiKey: String
) {

    @Provides
    @NetworkScope
    @Named("internet-connection-interceptor")
    fun provideInternetConnectionInterceptor(): Interceptor {
        return InternetConnectionInterceptor(
            context
        ).createInternetConnectionInterceptor()
    }

    @Provides
    @NetworkScope
    @Named("chucker-interceptor")
    fun provideChuckerInterceptor(): Interceptor {
        return ChuckerInterceptor.Builder(context).build()
    }

    @Provides
    @NetworkScope
    @Named("header-interceptor")
    fun provideHttpInterceptor(): Interceptor {
        return HeaderInterceptor(tmdbApiKey)
    }

    @Provides
    @NetworkScope
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    @NetworkScope
    fun provideOkHttpClient(
        @Named("header-interceptor") headerInterceptor: Interceptor,
        @Named("internet-connection-interceptor") internetConnectionInterceptor: Interceptor,
        @Named("chucker-interceptor") chuckerInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            this.addInterceptor(headerInterceptor)
                .addInterceptor(internetConnectionInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
            addInterceptor(chuckerInterceptor)
        }.build()
    }

    @Provides
    @NetworkScope
    @Named("tmdb-retrofit")
    fun provideGithubRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit {
        val baseUrl = HostUrl.TMDB_BASE_URL
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
