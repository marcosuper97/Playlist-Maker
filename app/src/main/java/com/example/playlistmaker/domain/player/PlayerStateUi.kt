package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.models.Track

sealed class PlayerStateUi {
    data class ReadyToPlay(
        val track: Track
    ) : PlayerStateUi()
    data class Play(val track: Track) : PlayerStateUi()
    data class Pause(val track: Track) : PlayerStateUi()
}