package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.PlayerInterractor
import com.example.playlistmaker.domain.player.UserMediaPlayer

class PlayerInterractorImpl(private val userMediaPlayerImpl: UserMediaPlayer): PlayerInterractor {
    override fun getPlayTimer(): Int {
       return userMediaPlayerImpl.getPlayTimer()
    }

    override fun getStatePlayer(): Int {
        return userMediaPlayerImpl.getStatePlayer()
    }

    override fun pauseMusic() {
        userMediaPlayerImpl.pauseMusic()
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

    override fun startMusic() {
        userMediaPlayerImpl.playMusic()
    }

    override fun setOnCompletionListener(listener: () -> Unit) {
        userMediaPlayerImpl.setOnCompletionListener(listener)
    }
}