package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInterractor
import com.example.playlistmaker.domain.api.UserMediaPlayer

class PlayerInterractorImpl(private val userMediaPlayerImpl: UserMediaPlayer):PlayerInterractor {
    override fun getPlayTimer(): Int {
       return userMediaPlayerImpl.getPlayTimer()
    }

    override fun getStatePlayer(): Int {
        return userMediaPlayerImpl.getStatePlayer()
    }

    override fun pausePlayer() {
        userMediaPlayerImpl.pausePlayer()
    }

    override fun playbackControl() {
        userMediaPlayerImpl.playbackControl()
    }

    override fun preparePlayer(trackPreviewUrl: String) {
        userMediaPlayerImpl.preparePlayer(trackPreviewUrl)
    }

    override fun release() {
        userMediaPlayerImpl.release()
    }

    override fun startPlayer() {
        userMediaPlayerImpl.startPlayer()
    }
}