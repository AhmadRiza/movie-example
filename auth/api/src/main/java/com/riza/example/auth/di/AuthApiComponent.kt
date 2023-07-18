package com.riza.example.auth.di

import com.riza.example.auth.navigator.AuthNavigator

/**
 * Created by ahmadriza on 18/07/23.
 */
interface AuthApiComponent {
    fun navigator(): AuthNavigator
}