package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.UserMediaPlayer

class UserMediaPlayerImpl() : UserMediaPlayer {

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var onCompletionListener: (() -> Unit)? = null

    override fun preparePlayer(trackPreviewUrl: String) {
        mediaPlayer.setDataSource(trackPreviewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            onCompletionListener?.invoke()
        }
    }

    override fun playMusic() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pauseMusic() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED

    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pauseMusic()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                playMusic()
            }
        }
    }

    override fun getStatePlayer(): Int {
        return playerState
    }

    override fun getPlayTimer():Int {
        val totalDuration = mediaPlayer.duration
        val currentPosition = mediaPlayer.currentPosition
        return totalDuration - currentPosition
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        onCompletionListener = listener
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}