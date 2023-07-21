package com.riza.example.network.interceptor

import com.riza.example.network.NetworkHeader
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by ahmadriza on 14/08/22.
 */
class HeaderInterceptor(
    private val tmdbApiKey: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        val request = builder.addHeader(NetworkHeader.CONTENT_TYPE, "application/json")
            .addHeader(NetworkHeader.ACCEPT, "application/json")
            .addHeader(NetworkHeader.AUTHORIZATION, "Bearer $tmdbApiKey")
            .build()

        return chain.proceed(request)
    }
}
