package com.riza.example.common.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import javax.inject.Inject

class ResourceProviderImpl @Inject constructor(private val context: Context) : ResourceProvider {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, arg: String): String {
        return context.getString(resId, arg)
    }

    override fun getString(resId: Int, vararg args: String): String {
        return context.getString(resId, *args)
    }

    override fun getStringArray(resId: Int): Array<String> {
        return context.resources.getStringArray(resId)
    }

    override fun getColor(resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    override fun parseColor(colorHexString: String): Int {
        return Color.parseColor(colorHexString)
    }

    override fun getDefaultAvatarUrl(name: String): String {
        return "https://ui-avatars.com/api/?name=$name&background=e8e8e8&color=4a4a4a"
    }
}
