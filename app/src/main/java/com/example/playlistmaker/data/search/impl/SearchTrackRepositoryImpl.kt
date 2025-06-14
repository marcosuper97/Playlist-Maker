package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.search.SearchTrackRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.util.SearchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class SearchTrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDatabase
) : SearchTrackRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                val idFavoriteTracks = database.trackDao().getAllFavoriteTracksId().first().toSet()
                with(response as TrackSearchResponse) {
                    val data = results.map {
                        Track(
                            isFavorite = idFavoriteTracks.contains(it.trackId),
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    emit(Resource.Success(data))
                }
            }

            -1 -> {
                emit(Resource.Error(SearchState.NetworkError))
            }

            else -> {
                emit(Resource.Error(SearchState.NetworkError))
            }
        }
    }
}