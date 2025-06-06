package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.playlistmaker.data.db.entities.ConnectionEntity
import com.example.playlistmaker.data.db.entities.PlaylistEntity
import com.example.playlistmaker.data.db.entities.TracksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
//Ниже введено много методов для манипуляции с сущностями. Основные методы в самом низу с аннотацией @Transaction

    //Это зона сущности TrackEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToTrackTable(track: TracksEntity)

    @Query("SELECT * FROM track_table")
    fun getAllTracks(): Flow<List<TracksEntity>>

    @Query("SELECT * FROM track_table WHERE isFavorite = 1 ORDER BY addedDate DESC")
    fun getAllFavoriteTracks(): Flow<List<TracksEntity>>

    @Query("SELECT trackId FROM track_table WHERE isFavorite = 1 ORDER BY addedDate DESC")
    fun getAllFavoriteTracksId(): Flow<List<Long>>

    @Query("SELECT EXISTS(SELECT 1 FROM track_table WHERE trackId = :trackId AND isFavorite = 1)")
    suspend fun isFavoriteTrack(trackId: Long): Boolean //ИСПОЛЬЗУЕТСЯ

    @Query("SELECT trackId FROM track_table")
    fun getAllTrackId(): Flow<List<Long>>

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrackFromTable(trackId: Long) //ИСПОЛЬЗУЕТСЯ

    @Query("SELECT * FROM track_table WHERE trackId = :trackId AND isFavorite = 1")
    suspend fun getFavoriteTrack(trackId: Long): TracksEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM track_table WHERE trackId=:trackId)")
    suspend fun isContainsInTrackTable(trackId: Long): Boolean //ИСПОЛЬЗУЕТСЯ


    //Это зона сущности PlaylistEntity
    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlist_table WHERE playlistId=:playlistId")
    fun getPlaylistEntity(playlistId: Long): Flow<PlaylistEntity>

    @Query("DELETE FROM playlist_table WHERE playlistId=:playlistId")
    suspend fun deletePlaylistFromTable(playlistId: Long)

    @Update
    suspend fun updatePlaylistInfo(playlistEntity: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createPlaylist(playlistEntity: PlaylistEntity)

    @Query("UPDATE playlist_table SET tracksCount=:trackCount WHERE playlistId=:playlistId")
    suspend fun updateTracksCount(trackCount: Long, playlistId: Long)


    //Это зона сущности ConnectionEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylistLink(connectionEntity: ConnectionEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addTrackToConnectionTable(connectionEntity: ConnectionEntity)

    @Query("DELETE FROM connection_table WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Long) //ИСПОЛЬЗУЕТСЯ

    @Query("SELECT EXISTS(SELECT * FROM connection_table WHERE trackId = :trackId LIMIT 1)")
    suspend fun findTrackInConnectionTable(trackId: Long): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM connection_table WHERE trackId =:trackId)")
    suspend fun isTrackInConnectionTable(trackId: Long): Boolean //ИСПОЛЬЗУЕТСЯ

    @Query("SELECT trackId FROM connection_table WHERE playlistId = :playlistId")
    suspend fun getTracksIdFromPlaylist(playlistId: Long): List<Long> //ИСПОЛЬЗУЕТСЯ

    @Query("DELETE FROM connection_table WHERE playlistId=:playlistId")
    suspend fun deletePlaylistFromConectionTable(playlistId: Long)

    @Query("SELECT COUNT (*) FROM connection_table WHERE playlistId = :playlistId")
    suspend fun getTracksCountInPlaylist(playlistId: Long): Long


    //Transaction запросы
    @Transaction
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        deleteTrackFromPlaylist(playlistId, trackId)
        if (!isTrackInConnectionTable(trackId) && !isFavoriteTrack(trackId)) {
            deleteTrackFromTable(trackId)
        }
    }

    @Transaction
    suspend fun deletePlaylist(playlistId: Long) {
        val tracksIdFromPlaylist: List<Long> = getTracksIdFromPlaylist(playlistId)
        if (tracksIdFromPlaylist.isNotEmpty()) {
            tracksIdFromPlaylist.forEach() { trackId ->
                deleteTrackFromPlaylist(playlistId, trackId)
                if (!isTrackInConnectionTable(trackId) && !isFavoriteTrack(trackId)) deleteTrackFromTable(
                    trackId
                )
            }
            deletePlaylistFromTable(playlistId)
        } else deletePlaylistFromTable(playlistId)
    }

    @Transaction
    suspend fun addTrackToPlaylist(track: TracksEntity, playlistId: Long) {
        when {
            isContainsInTrackTable(track.trackId) -> addTrackToConnectionTable(
                ConnectionEntity(
                    track.trackId,
                    playlistId
                )
            )

            else -> {
                addTrackToTrackTable(track)
                addTrackToConnectionTable(ConnectionEntity(track.trackId, playlistId))
            }
        }
    }

    @Transaction
    suspend fun markAsFavorite(trackEntity: TracksEntity) {
        val isContainsInPlaylist: Boolean = findTrackInConnectionTable(trackEntity.trackId)
        when {
            isContainsInPlaylist -> addTrackToTrackTable(trackEntity)
            !trackEntity.isFavorite -> deleteTrackFromTable(trackEntity.trackId)
            else -> addTrackToTrackTable(trackEntity)
        }
    }

    @Transaction
    @Query("SELECT * FROM playlist_table WHERE playlistId = :playlistId")
    fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracksDb>

    @Transaction
    suspend fun updateTracksCountInPlaylist(playlistId: Long) {
        val tracksCount = getTracksCountInPlaylist(playlistId)
        updateTracksCount(tracksCount, playlistId)
    }

}
