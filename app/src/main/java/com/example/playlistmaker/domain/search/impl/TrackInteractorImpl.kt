package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.SearchTrackRepository
import com.example.playlistmaker.domain.search.TrackInteractor
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: SearchTrackRepository): TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }

    override fun shutDown() {
        executor.shutdown()
    }

}