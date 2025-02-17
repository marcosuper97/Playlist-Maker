package com.example.playlistmaker.presentation

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.api.UserMediaPlayer
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var mainThreadHandler: Handler? = null
    private lateinit var mediaPlayer:UserMediaPlayer

    override fun onPause() {
        super.onPause()
        mainThreadHandler?.removeCallbacksAndMessages(null)
        mediaPlayer.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.playerActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainThreadHandler = Handler(Looper.getMainLooper())
        mediaPlayer = Creator.mediaPlayer()
        val track = mediaPlayer.getTrack(intent.getStringExtra("track").toString())
        mediaPlayer.preparePlayer(track.previewUrl)

        fun getCoverArtwork() = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        binding.songName.text = track.trackName
        binding.executor.text = track.artistName
        binding.durationTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.albumName.text = track.collectionName
        binding.releaseYearNumber.text = track.releaseDate.take(4)
        binding.genreName.text = track.primaryGenreName
        binding.countryName.text = track.country

        Glide.with(binding.cover)
            .load(getCoverArtwork())
            .placeholder(R.drawable.player_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.cover)

        binding.playerBack.setOnClickListener() {
            finish()
        }

        binding.addButton.setOnClickListener() { }

        binding.playPauseButton.setOnClickListener() {
            mediaPlayer.playbackControl()
            if (mediaPlayer.getStatePlayer() == STATE_PLAYING) {
                binding.playPauseButton.setImageDrawable(getDrawable(R.drawable.button_pause))
                mediaPlayer.playTimer()
                mainThreadHandler?.post { timerSong }
            } else {
                binding.playPauseButton.setImageDrawable(getDrawable(R.drawable.button_play))
            }
        }
        binding.likeButton.setOnClickListener() { }
    }

    private val timerSong = object : Runnable {
        override fun run() {
            if (mediaPlayer.getStatePlayer() == STATE_PLAYING) {
                binding.time.text = "1"
                mainThreadHandler?.postDelayed(this, TIME_UPDATE)
            }
            if (mediaPlayer.getStatePlayer() == STATE_PAUSED) {
                mainThreadHandler?.removeCallbacks(this)
            }
        }
    }

    companion object {
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_UPDATE = 500L
        private const val TIME_DEF = "00:30"
        private const val TIME_END = "00:00"
    }
}