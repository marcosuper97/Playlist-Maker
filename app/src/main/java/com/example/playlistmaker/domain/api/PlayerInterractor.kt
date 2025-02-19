package com.example.playlistmaker.domain.api

interface PlayerInterractor {
    fun getPlayTimer(): Int

    fun getStatePlayer(): Int

    fun pausePlayer()

    fun playbackControl()

    fun preparePlayer(trackPreviewUrl: String)

    fun release()

    fun startPlayer()
}
