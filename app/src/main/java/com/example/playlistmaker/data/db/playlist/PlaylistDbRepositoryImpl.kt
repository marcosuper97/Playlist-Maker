package com.example.playlistmaker.data.db.playlist

import android.database.sqlite.SQLiteConstraintException
import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.domain.db.playlist.PlaylistDbRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistWithTracks
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistDbRepositoryImpl(
    private val database: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackDbConverter: TrackDbConverter
) : PlaylistDbRepository {

    override fun createPlaylist(
        playlist: Playlist
    ): Flow<Result<String>> = flow {
        try {
            database.trackDao().createPlaylist(playlistDbConverter.map(playlist))
            emit(Result.success("Плейлист ${playlist.playlistName} создан"))
        } catch (e: SQLiteConstraintException) {
            emit(Result.failure(Exception("Плейлист ${playlist.playlistName} уже существует.")))
        }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        database.trackDao().deletePlaylist(playlistId)
    }

    override fun getPlaylist(playlistId: Long): Flow<Playlist> =
        database.trackDao().getPlaylistEntity(playlistId).map {
            playlistDbConverter.map(it)
        }

    override fun getAllPlaylists(): Flow<List<Playlist>> =
        database.trackDao().getAllPlaylists().map { playlistEntities ->
            playlistEntities.map {
                playlistDbConverter.map(it)
            }
        }

    override fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks> =
        database.trackDao().getPlaylistWithTracks(playlistId).map { playlistWithTracksDb ->
            playlistDbConverter.map(playlistWithTracksDb)
        }

    override fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    ): Flow<Result<String>> = flow {
        val trackEntity = trackDbConverter.map(track)
        try {
            database.trackDao().addTrackToPlaylist(trackEntity, playlist.playlistId)
            database.trackDao().updateTracksCountInPlaylist(playlist.playlistId)
            emit(Result.success("Добавлено в плейлист \"${playlist.playlistName}\""))
        } catch (e: SQLiteConstraintException) {
            emit(Result.failure(Exception("Трек уже добавлен в плейлист \"${playlist.playlistName}\"")))
        }
    }
}