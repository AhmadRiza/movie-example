package com.riza.example.explore.data.usecase

import com.riza.example.common.base.NonSuspendingUseCase
import com.riza.example.explore.di.ExploreServiceScope
import javax.inject.Inject

/**
 * Created by ahmadriza on 21/07/23.
 */
/**
 * Get mapped emoticon from movie genre name
 */
@ExploreServiceScope
class GetGenreEmoticon @Inject constructor(): NonSuspendingUseCase<String, String>() {

    override fun build(params: String?): String {
        requireNotNull(params)
        return emoticonMap.getOrElse(params) {
            "🍿"
        }
    }

    private val emoticonMap = mapOf(
        "action" to "✊",
        "comedy" to "🤣",
        "adventure" to "🧗‍",
        "animation" to "🧚‍",
        "crime" to "🦹‍",
        "documentary" to "🎬",
        "drama" to "💃",
        "family" to "👨‍👩‍👧‍👦",
        "fantasy" to "🛸",
        "history" to "🗿",
        "horror" to "🧛‍",
        "music" to "👨‍🎤",
        "mystery" to "🕵️‍",
        "romance" to "👰‍",
        "science fiction" to "👽",
        "tv movie" to "🎭",
        "thriller" to "🦈",
        "war" to "🔫",
        "western" to "🤠"
    )
}