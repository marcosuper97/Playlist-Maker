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
import com.example.playlistmaker.domain.impl.PlayerInterractorImpl
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var mainThreadHandler: Handler? = null
    private var mediaPlayer: PlayerInterractorImpl? = null
    private val playerTrack = Creator.getPlayerTrack()
    private val trackCover = Creator.trackCover()

    override fun onPause() {
        super.onPause()
        mainThreadHandler?.removeCallbacksAndMessages(null)
        mediaPlayer?.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
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
        val track = playerTrack.get(intent.getStringExtra("track").toString())
        mediaPlayer = Creator.getPlayerInterractor()
        mediaPlayer?.preparePlayer(track.previewUrl)
        binding.songName.text = track.trackName
        binding.executor.text = track.artistName
        binding.durationTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.albumName.text = track.collectionName
        binding.releaseYearNumber.text = track.releaseDate.take(4)
        binding.genreName.text = track.primaryGenreName
        binding.countryName.text = track.country

        Glide.with(binding.cover)
            .load(trackCover.editFormat(track.artworkUrl100))
            .placeholder(R.drawable.player_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.cover)

        binding.playerBack.setOnClickListener {
            finish()
        }
        binding.addButton.setOnClickListener { }
        binding.playPauseButton.setOnClickListener {
            mediaPlayer?.playbackControl()
            if (mediaPlayer?.getStatePlayer() == STATE_PLAYING) {
                binding.playPauseButton.setImageDrawable(getDrawable(R.drawable.button_pause))
                mainThreadHandler?.post { songRunTime.run() }
            } else {
                binding.playPauseButton.setImageDrawable(getDrawable(R.drawable.button_play))
            }
        }
        binding.likeButton.setOnClickListener { }
    }

    private fun setDefaultStatus(){
        binding.time.text = TIME_DEF
        binding.playPauseButton.setImageDrawable(getDrawable(R.drawable.button_play))
    }

    private val songRunTime = object : Runnable {
        override fun run() {
            if (mediaPlayer?.getStatePlayer() == STATE_PLAYING) {
                var time = mediaPlayer?.getPlayTimer()
                binding.time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
                mainThreadHandler?.postDelayed(this, TIME_UPDATE)
            }
            if (mediaPlayer?.getStatePlayer() == STATE_PAUSED) {
                mainThreadHandler?.removeCallbacks(this)
            }
            if (mediaPlayer?.getStatePlayer() == STATE_PREPARED)
            {
              setDefaultStatus()
            }
        }
    }

    companion object {
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val STATE_PREPARED = 1
        private const val TIME_UPDATE = 500L
        private const val TIME_DEF = "00:30"
    }
}