package com.example.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.player.GetPlayerTrack
import com.example.playlistmaker.domain.player.PlayerStateUi
import com.example.playlistmaker.domain.player.impl.PlayerInterractorImpl
import com.example.playlistmaker.util.Creator
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val trackJson: String,
    private var mediaPlayer: PlayerInterractorImpl?,
    private val playerTrack: GetPlayerTrack,
) : ViewModel() {
    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacksAndMessages(null)
    }

    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }
    private val _playerStateUi = MutableLiveData<PlayerStateUi>()
    val playerStateUi: LiveData<PlayerStateUi> get() = _playerStateUi

    private val _timer = MutableLiveData<String>()
    val timer: LiveData<String> get() = _timer

    init {
        playerTrack.get(trackJson,
            onComplete = { track ->
                _playerStateUi.postValue(
                    PlayerStateUi.ReadyToPlay(track)
                )
                mediaPlayer?.preparePlayer(track.previewUrl)
                mediaPlayer?.setOnCompletionListener {
                    _playerStateUi.postValue(
                        PlayerStateUi.ReadyToPlay(
                            playerTrack.getWithoutCallback(
                                trackJson
                            )
                        )
                    )
                }
            }
        )
    }


    private fun startTimer(onTick: (String) -> Unit) {
        val runnable = object : Runnable {
            override fun run() {
                val actualTime = mediaPlayer?.getPlayTimer()
                if (actualTime!! > 0) {
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
        if(mediaPlayer?.getStatePlayer() == STATE_PLAYING) {
            handler.post(runnable)
        }
    }

    private fun stopTimer() {
        handler.removeCallbacksAndMessages(null)
    }

    fun onClickPlayMusic() {
        if (mediaPlayer?.getStatePlayer() == STATE_PREPARED || mediaPlayer?.getStatePlayer() == STATE_PAUSED) {
            _playerStateUi.postValue(
                PlayerStateUi.Play(playerTrack.getWithoutCallback(trackJson))
            )
            mediaPlayer?.startMusic()
            startTimer() { onTick ->
                _timer.postValue(onTick)
            }
        } else if (mediaPlayer?.getStatePlayer() == STATE_PLAYING) {
            _playerStateUi.postValue(
                PlayerStateUi.Pause(playerTrack.getWithoutCallback(trackJson))
            )
            mediaPlayer?.pauseMusic()
            stopTimer()
        }
    }

    companion object {
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val STATE_PREPARED = 1
        private const val INTERVAL: Long = 1000L

        fun getViewModelFactory(trackId: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = Creator.getPlayerInterractor()
                val trackPlayer = Creator.getPlayerTrack()

                PlayerViewModel(
                    trackId,
                    interactor,
                    trackPlayer,
                )
            }
        }
    }
}