package com.riza.example.network.mapper

import com.riza.example.network.HostUrl

/**
 * Created by ahmadriza on 20/07/23.
 */

enum class TMDBImageSize {
    W500, W300, ORIGINAL
}

fun String.toImdbImageUrl(size: TMDBImageSize = TMDBImageSize.W500): String {
    return "${HostUrl.TMDB_BASE_IMAGE_URL}${size.name.lowercase()}$this"
}