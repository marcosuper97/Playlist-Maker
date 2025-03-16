package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.data.player.UserMediaPlayerRepository
import com.example.playlistmaker.util.MediaPlayerState

class UserMediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) :
    UserMediaPlayerRepository {

    private val _mediaPlayerState = MutableLiveData<MediaPlayerState>(MediaPlayerState.Default)
    override val mediaPlayerState: LiveData<MediaPlayerState> get() = _mediaPlayerState

    override fun preparePlayer(trackPreviewUrl: String) {
        mediaPlayer.setDataSource(trackPreviewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            _mediaPlayerState.postValue(MediaPlayerState.Prepared)
        }
        mediaPlayer.setOnCompletionListener {
            _mediaPlayerState.postValue(MediaPlayerState.Prepared)
        }
    }

    override fun playMusic() {
        mediaPlayer.start()
        _mediaPlayerState.postValue(MediaPlayerState.Playing)
    }

    override fun pauseMusic() {
        mediaPlayer.pause()
        _mediaPlayerState.postValue(MediaPlayerState.Paused)
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun reset() {
        mediaPlayer.reset()
    }

    override fun playbackControl() {
        when (mediaPlayerState.value) {
            MediaPlayerState.Playing -> {
                pauseMusic()
            }
            MediaPlayerState.Prepared, MediaPlayerState.Paused -> {
                playMusic()
            }

            MediaPlayerState.Default -> {
                println("ДЕФОЛТ РЕПОЗИТОРИЙ")
            }
            null -> {
                println("НУЛЛ РЕПОЗИТОРИЙ")
            }
        }
    }

    override fun getPlayTimer():Int {
        val totalDuration = mediaPlayer.duration
        val currentPosition = mediaPlayer.currentPosition
        println(totalDuration - currentPosition)
        return totalDuration - currentPosition
    }
}