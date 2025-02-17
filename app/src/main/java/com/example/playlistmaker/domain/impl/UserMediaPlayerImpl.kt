package com.example.playlistmaker.domain.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.data.dto.GsonClient
import com.example.playlistmaker.domain.api.UserMediaPlayer
import com.example.playlistmaker.domain.models.Track

class UserMediaPlayerImpl() : UserMediaPlayer {

    val mediaHandler = Handler(Looper.getMainLooper())
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    override fun preparePlayer(stringUrl: String) {
        mediaPlayer.reset()
        mediaPlayer.setDataSource(stringUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED

    }

    override fun release() {
        mediaPlayer.release()
    }



    override fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private val timerSong = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                val totalDuration = mediaPlayer.duration
                val currentPosition = mediaPlayer.currentPosition
                val totalTime = totalDuration - currentPosition
                mediaHandler.postDelayed(this, TIME_UPDATE)
            }
            if (playerState == STATE_PAUSED) {
                mediaHandler.removeCallbacks(this)
            }
        }
    }


    override fun getTrack(info: String): Track {
        return GsonClient.objectFromJson(info)
    }

    override fun getStatePlayer(): Int {
        return playerState
    }

    override fun playTimer() {
        timerSong.run()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_UPDATE = 500L
        private const val TIME_DEF = "00:30"
        private const val TIME_END = "00:00"
    }
}