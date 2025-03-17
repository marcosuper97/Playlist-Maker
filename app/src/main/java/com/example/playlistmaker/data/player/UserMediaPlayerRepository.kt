package com.example.playlistmaker.data.player

import androidx.lifecycle.LiveData
import com.example.playlistmaker.util.MediaPlayerState


interface UserMediaPlayerRepository {

    val mediaPlayerState: LiveData<MediaPlayerState>

    fun pauseMusic()

    fun playMusic()

    fun playbackControl()

    fun preparePlayer(trackPreviewUrl: String)

    fun release() {}

    fun getPlayTimer():Int

    fun reset()

}