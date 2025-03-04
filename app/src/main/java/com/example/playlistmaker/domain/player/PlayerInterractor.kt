package com.example.playlistmaker.domain.player

interface PlayerInterractor {
    fun getPlayTimer(): Int

    fun getStatePlayer(): Int

    fun pauseMusic()

    fun playbackControl()

    fun preparePlayer(trackPreviewUrl: String)

    fun release()

    fun startMusic()

    fun setOnCompletionListener(listener: () -> Unit)
}
