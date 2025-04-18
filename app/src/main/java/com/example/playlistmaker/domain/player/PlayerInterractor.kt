package com.example.playlistmaker.domain.player

import androidx.lifecycle.LiveData
import com.example.playlistmaker.util.MediaPlayerState

interface PlayerInterractor {

    val mediaPlayerState: LiveData<MediaPlayerState>

    fun getPlayTimer(): Int

    fun playbackControl()

    fun preparePlayer(trackPreviewUrl: String)

    fun release()

    fun startMusic()

    fun pauseMusic()
}
