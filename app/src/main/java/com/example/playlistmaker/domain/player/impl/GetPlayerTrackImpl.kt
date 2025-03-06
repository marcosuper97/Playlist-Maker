package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.data.dto.GsonClient
import com.example.playlistmaker.domain.player.GetPlayerTrack
import com.example.playlistmaker.domain.models.Track

class GetPlayerTrackImpl() : GetPlayerTrack {
    override fun get(string: String, onComplete: (Track) -> Unit) {
        val formatedTrack = editCoverSize(string)
        val track: Track = GsonClient.fromJsonToPlayer(formatedTrack)
        onComplete(track)
    }

    override fun getWithoutCallback(string: String): Track {
        val formatedTrack = editCoverSize(string)
        return GsonClient.fromJsonToPlayer(formatedTrack)
    }

    private fun editCoverSize(string: String): String {
        val regex = Regex("""\d+x\d+bb\.jpg""")
        return regex.replace(string, FORMAT_SIZE)
    }
    companion object{
        private const val FORMAT_SIZE = "512x512bb.jpg"
    }
}