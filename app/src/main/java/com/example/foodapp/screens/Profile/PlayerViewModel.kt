package com.example.foodapp.screens.Profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem


class PlayerViewModel : ViewModel() {

    var player: ExoPlayer? = null
    var playbackPosition = 0L
    var playWhenReady = true

    fun initializePlayer(context: Context) {
        if (player == null) {
            player = ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(
                    "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
                )
                setMediaItem(mediaItem)
                prepare()
                seekTo(playbackPosition)
                playWhenReady = true
            }
        }
    }

    fun pausePlayer() {
        player?.pause()
    }

    fun saveState() {
        player?.let {
            playbackPosition = it.currentPosition
            playWhenReady = it.playWhenReady
        }
    }

}