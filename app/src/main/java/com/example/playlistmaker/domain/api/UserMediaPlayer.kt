package com.example.playlistmaker.domain.api


interface UserMediaPlayer {

    fun pausePlayer()

    fun startPlayer()

    fun playbackControl()

    fun preparePlayer(trackPreviewUrl: String)

    fun release() {}

    fun getStatePlayer():Int

    fun getPlayTimer():Int

}