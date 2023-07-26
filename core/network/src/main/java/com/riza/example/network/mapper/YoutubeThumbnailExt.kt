package com.riza.example.network.mapper

import com.riza.example.network.HostUrl

/**
 * Created by ahmadriza on 21/07/23.
 */

/**
 * Generate youtube thumbnail from youtube video ID
 */
fun String.toYoutubeThumbnail(): String {
    return HostUrl.YOUTUBE_THUMBNAIL_URL.replace("{id}", this)
}
