package com.example.foodapp.screens.Profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem
import com.example.foodapp.screens.Constant.Constant
import kotlinx.coroutines.flow.MutableStateFlow


class PlayerViewModel : ViewModel() {

    var player: ExoPlayer? = null
    private val playerPosition = MutableStateFlow(0L)

    fun updatePosition() {
        playerPosition.value = player?.currentPosition ?: 0L
    }
    fun initializePlayer(context: Context) {
        if (player == null) {
            player = ExoPlayer.Builder(context)
                .setSeekBackIncrementMs(10_000)
                .setSeekForwardIncrementMs(10_000)
                .build()
                .apply {
                    val mediaItem = MediaItem.fromUri(Constant.VideoUrl)
                    setMediaItem(mediaItem)
                    prepare()
                    seekTo(playerPosition.value)
                    playWhenReady = true
                }
        }
    }
    fun switchVideo(url: String) {
        player?.apply {
            stop()
            clearMediaItems()
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = true
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