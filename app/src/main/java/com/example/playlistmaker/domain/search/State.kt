package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track

sealed class State {
    data class ShowSearchContent(val tracks: List<Track>):State()
    data class ShowHistoryContent(val tracks: List<Track>):State()
    data object Loading : State()
    data object NothingFound: State()
    data object NetworkError: State()
    data object EmptyScreen: State()
}