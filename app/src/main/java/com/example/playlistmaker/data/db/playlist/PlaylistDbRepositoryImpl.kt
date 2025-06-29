package com.example.playlistmaker.data.db.playlist

import android.database.sqlite.SQLiteConstraintException
import androidx.room.withTransaction
import com.example.playlistmaker.data.converters.PlaylistDbConverter
import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entities.ConnectionEntity
import com.example.playlistmaker.data.db.entities.TracksEntity
import com.example.playlistmaker.domain.db.playlist.PlaylistDbRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistWithTracks
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistDbRepositoryImpl(
    private val db: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackDbConverter: TrackDbConverter
) : PlaylistDbRepository {

    override fun createPlaylist(
        playlist: Playlist
    ): Flow<Result<String>> = flow {
        try {
            db.playlistDao().createPlaylist(playlistDbConverter.map(playlist))
            emit(Result.success("Плейлист ${playlist.playlistName} создан"))
        } catch (e: SQLiteConstraintException) {
            emit(Result.failure(Exception("Плейлист ${playlist.playlistName} уже существует.")))
        }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        db.withTransaction {
            val tracksInPlaylist = db.connectionTableDao().getTracksIdFromPlaylist(playlistId)

            when (tracksInPlaylist.isNotEmpty()) {
                true -> {
                    tracksInPlaylist.forEach() { trackId ->
                        db.connectionTableDao().deleteTrackFromPlaylist(playlistId, trackId)
                        if (!db.connectionTableDao()
                                .isTrackInConnectionTable(trackId) && !db.trackDao()
                                .isFavoriteTrack(trackId)
                        ) db.trackDao().deleteTrackFromTable(
                            trackId
                        )
                    }
                    db.playlistDao().deletePlaylistFromTable(playlistId)
                }

                false -> db.playlistDao().deletePlaylistFromTable(playlistId)
            }
        }
    }


    override fun getPlaylist(playlistId: Long): Flow<Playlist> =
        db.playlistDao().getPlaylistEntity(playlistId).map {
            playlistDbConverter.map(it)
        }

    override fun getAllPlaylists(): Flow<List<Playlist>> =
        db.playlistDao().getAllPlaylists().map { playlistEntities ->
            playlistEntities.map {
                playlistDbConverter.map(it)
            }
        }

    override fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks?> = flow {
        val connections = db.connectionTableDao().getTracksFromPlaylist(playlistId)
            .associateBy { it.trackId }
        db.playlistDao().getPlaylistWithTracks(playlistId).collect { playlistWithTracksDb ->
            val result = when (playlistWithTracksDb) {
                null -> null
                else -> {
                    val sortedTracks = playlistWithTracksDb.tracks.sortedByDescending { track ->
                        connections[track.trackId]?.addedDate ?: 0L
                    }
                    val sortedPlaylist = playlistWithTracksDb.copy(tracks = sortedTracks)
                    playlistDbConverter.map(sortedPlaylist)
                }
            }
            emit(result)
        }
    }

    override fun addTrackToPlaylist(
        track: Track,
        playlist: Playlist
    ): Flow<Result<String>> = flow {
        val trackEntity = trackDbConverter.map(track)
        try {
            db.withTransaction {
                addTrackToPlaylistInternal(trackEntity, playlist.playlistId)
            }
            emit(Result.success("Добавлено в плейлист \"${playlist.playlistName}\""))
        } catch (e: SQLiteConstraintException) {
            emit(Result.failure(Exception("Трек уже добавлен в плейлист \"${playlist.playlistName}\"")))
        }
    }

    override suspend fun deleteTrackFromPlaylist(
        playlistId: Long,
        trackId: Long
    ) {
        db.withTransaction {
            db.connectionTableDao().deleteTrackFromPlaylist(playlistId, trackId)

            if (!db.connectionTableDao().isTrackInConnectionTable(trackId) &&
                !db.trackDao().isFavoriteTrack(trackId)
            ) {
                db.trackDao().deleteTrackFromTable(trackId)
            }

            val tracksCount = db.connectionTableDao().getTracksCountInPlaylist(playlistId)
            db.playlistDao().updateTracksCount(tracksCount, playlistId)
        }
    }

    override suspend fun updatePlaylistInfo(playlist: Playlist) {
        db.playlistDao().updatePlaylistInfo(playlistDbConverter.map(playlist))
    }

    private suspend fun addTrackToPlaylistInternal(trackEntity: TracksEntity, playlistId: Long) {
        when {
            db.trackDao()
                .isContainsInTrackTable(trackEntity.trackId) -> db.connectionTableDao()
                .addTrackToConnectionTable(
                    ConnectionEntity(
                        trackEntity.trackId,
                        playlistId
                    )
                )

            else -> {
                db.trackDao().addTrackToTrackTable(trackEntity)
                db.connectionTableDao().addTrackToConnectionTable(
                    ConnectionEntity(
                        trackEntity.trackId,
                        playlistId
                    )
                )
            }
        }
        val tracksCount =
            db.connectionTableDao().getTracksCountInPlaylist(playlistId)
        db.playlistDao().updateTracksCount(tracksCount, playlistId)
    }
}