package com.example.playlistmaker.data.db.favorite

import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entities.TracksEntity
import com.example.playlistmaker.domain.db.favorite.FavoriteControlRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteControlRepositoryImpl(
    private val database: AppDatabase,
    private val trackDbConverter: TrackDbConverter,
) : FavoriteControlRepository {

    override fun getFavoriteTracks(): Flow<List<Track>> = database.trackDao().getAllFavoriteTracks()
        .map { entities ->
            convertFromTrackEntity(entities)
        }

    override suspend fun favoriteControl(track: Track) {
        database.trackDao().markAsFavorite(convertToTrackEntity(track))
    }

    override fun getIdFavoriteTracks(): Flow<List<Long>> {
        return database.trackDao().getAllTrackId()
    }

    private fun convertFromTrackEntity(tracks: List<TracksEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

    private fun convertToTrackEntity(track: Track): TracksEntity {
        return trackDbConverter.map(track)
    }
}