package com.riza.example.detail.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.riza.example.detail.ui.R

/**
 * Created by ahmadriza on 21/07/23.
 */
class YoutubePlayerActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_VIDEO_ID = "EXTRA_VIDEO_ID"
        fun createIntent(context: Context, videoId: String): Intent {
            return Intent(context, YoutubePlayerActivity::class.java).apply {
                putExtra(EXTRA_VIDEO_ID, videoId)
            }
        }
    }

    private val videoId: String by lazy {
        intent.getStringExtra(EXTRA_VIDEO_ID).orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_youtube_player)
        val youTubePlayerView = findViewById<YouTubePlayerView>(R.id.youtubePlayer)
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }
}
