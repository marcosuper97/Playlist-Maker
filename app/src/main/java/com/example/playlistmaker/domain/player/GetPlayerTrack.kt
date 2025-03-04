package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.models.Track

interface GetPlayerTrack {
    fun get(string:String, onComplete: (Track) -> Unit)
    fun getWithoutCallback(string: String):Track
}