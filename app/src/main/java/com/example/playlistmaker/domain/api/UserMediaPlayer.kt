package com.example.playlistmaker.domain.api


import com.example.playlistmaker.domain.models.Track

interface UserMediaPlayer {

    fun pausePlayer()

    fun startPlayer()

    fun playbackControl()

    fun getTrack(info: String): Track

    fun preparePlayer(string: String)

    fun release() {}

    fun getStatePlayer():Int

    fun playTimer()

}