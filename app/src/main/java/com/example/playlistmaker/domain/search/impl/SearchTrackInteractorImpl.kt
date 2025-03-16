package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.data.search.SearchTrackRepository
import com.example.playlistmaker.domain.search.SearchTrackInteractor
import java.util.concurrent.Executors

class SearchTrackInteractorImpl(private val repository: SearchTrackRepository): SearchTrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: SearchTrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }

    override fun shutDown() {
        executor.shutdown()
    }

}