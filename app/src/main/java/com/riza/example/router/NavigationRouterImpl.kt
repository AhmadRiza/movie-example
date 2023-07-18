package com.riza.example.router

import android.content.Context
import android.content.Intent
import com.riza.example.common.router.NavigationRouter
import com.riza.example.main.MainActivity

/**
 * Created by ahmadriza on 15/08/22.
 * Copyright (c) 2022 Kitabisa. All rights reserved.
 */
class NavigationRouterImpl : NavigationRouter {
    override fun goToMain(context: Context) {
        context.run {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}
