package com.riza.example.common.util

interface ResourceProvider {
    fun getString(resId: Int): String
    fun getString(resId: Int, arg: String): String
    fun getString(resId: Int, vararg args: String): String
    fun getStringArray(resId: Int): Array<String>
    fun getColor(resId: Int): Int
    /**
     * Parse the color string, and return the corresponding color-int.
     * If the string cannot be parsed, throws an IllegalArgumentException
     * exception. Supported formats are:
     *
     * #RRGGBB
     * #AARRGGBB
     *
     * */
    fun parseColor(colorHexString: String): Int
    fun getDefaultAvatarUrl(name: String): String
}
