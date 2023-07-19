package com.riza.example.explore.di

import com.riza.example.explore.navigator.ExploreNavigator

/**
 * Created by ahmadriza on 18/07/23.
 */
interface ExploreApiComponent {
    fun navigator(): ExploreNavigator
}