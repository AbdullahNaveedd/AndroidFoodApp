package com.example.foodapp.screens.Profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow


class PlayerViewModel : ViewModel() {

    var player: ExoPlayer? = null
    private val playerPosition = MutableStateFlow(0L)

    fun updatePosition() {
        playerPosition.value = player?.currentPosition ?: 0L
    }
    fun initializePlayer(context: Context) {
        if (player == null) {
            player = ExoPlayer.Builder(context).build().apply {
                val mediaItem = MediaItem.fromUri(
                    "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
                )
                setMediaItem(mediaItem)
                prepare()
                seekTo(playerPosition.value)
                playWhenReady = true
            }
        }
    }
    fun pausePlayer() {
        player?.pause()
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
        player = null
    }

}