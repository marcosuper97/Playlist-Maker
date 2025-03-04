package com.example.playlistmaker.domain.player


interface UserMediaPlayer {

    fun pauseMusic()

    fun playMusic()

    fun playbackControl()

    fun preparePlayer(trackPreviewUrl: String)

    fun release() {}

    fun getStatePlayer():Int

    fun getPlayTimer():Int

    fun setOnCompletionListener(listener: () -> Unit)

}