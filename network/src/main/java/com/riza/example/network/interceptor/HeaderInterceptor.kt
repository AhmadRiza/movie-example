package com.riza.example.network.interceptor

import com.riza.example.network.NetworkHeader
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by ahmadriza on 14/08/22.
 * Copyright (c) 2022 Kitabisa. All rights reserved.
 */
class HeaderInterceptor(
    private val githubToken: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        val request = builder.addHeader(NetworkHeader.CONTENT_TYPE, "application/json")
            .addHeader(NetworkHeader.ACCEPT, "application/json")
            .addHeader(NetworkHeader.AUTHORIZATION, githubToken)
            .build()

        return chain.proceed(request)
    }
}
