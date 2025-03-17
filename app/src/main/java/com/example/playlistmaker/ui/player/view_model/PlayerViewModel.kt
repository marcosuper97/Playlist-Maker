package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.GetTrackInteractor
import com.example.playlistmaker.domain.player.PlayerInterractor
import com.example.playlistmaker.domain.player.PlayerStateUi
import com.example.playlistmaker.util.MediaPlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val trackJson: String,
    private var mediaPlayer: PlayerInterractor?,
    private val playerTrack: GetTrackInteractor,
) : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        handler.removeCallbacksAndMessages(null)
    }

    private val trackToPlay = playerTrack.getWithoutCallback(trackJson)
    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    private val _playerStateUi = MutableLiveData<PlayerStateUi>()
    val playerStateUi: LiveData<PlayerStateUi> get() = _playerStateUi

    init {

        mediaPlayer?.mediaPlayerState?.observeForever { state ->
            when (state) {
                MediaPlayerState.Default -> {
                    playerTrack.get(trackJson,
                        onComplete = { track ->
                            mediaPlayer?.preparePlayer(track.previewUrl)
                        }
                    )
                }

                MediaPlayerState.Paused -> stopTimer()

                MediaPlayerState.Playing -> {
                    startTimer() { onTick ->
                        _playerStateUi.postValue(
                            PlayerStateUi.Play(trackToPlay, onTick)
                        )
                    }
                }

                MediaPlayerState.Prepared -> {
                    handler.removeCallbacksAndMessages(null)
                    _playerStateUi.postValue(
                        PlayerStateUi.ReadyToPlay(
                            trackToPlay
                        )
                    )
                }
            }
        }
    }

    private fun startTimer(onTick: (String) -> Unit) {
        val runnable = object : Runnable {
            override fun run() {
                val actualTime = mediaPlayer?.getPlayTimer()
                if (actualTime != null && actualTime > 0) {
                    val formattedTime = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(actualTime)
                    onTick(formattedTime)
                    handler.postDelayed(this, INTERVAL)
                } else {
                    onTick("00:30")
                    handler.removeCallbacks(this)
                }
            }
        }
        handler.post(runnable)
    }

    private fun stopTimer() {
        _playerStateUi.postValue(PlayerStateUi.Pause(trackToPlay))
        handler.removeCallbacksAndMessages(null)
    }

    fun onClickPlayMusic() {
        mediaPlayer?.playbackControl()
    }

    fun activityOnPause() {
        if (mediaPlayer?.mediaPlayerState?.value == MediaPlayerState.Playing) {
            mediaPlayer?.pauseMusic()
        }
    }

    companion object {
        private const val INTERVAL: Long = 1000L
    }
}