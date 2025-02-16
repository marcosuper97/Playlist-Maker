package com.example.playlistmaker.presentation

import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.data.dto.GsonClient
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private var mainThreadHandler: Handler? = null
    private lateinit var track: Track
    lateinit var playerTime: TextView
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var url: String? = null
    private lateinit var playPauseButton: ImageView

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.playerActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mainThreadHandler = Handler(Looper.getMainLooper())
        val trackJson = intent.getStringExtra("track")
        if (trackJson != null) {
            track = GsonClient.objectFromJson(trackJson)
        }
        val playerBack = findViewById<Toolbar>(R.id.player_back)
        val songName = findViewById<TextView>(R.id.song_name)
        val executor = findViewById<TextView>(R.id.executor)
        val addButton = findViewById<ImageView>(R.id.add_button)
        val likeButton = findViewById<ImageView>(R.id.like_button)
        playerTime = findViewById(R.id.time)
        val duration = findViewById<TextView>(R.id.duration_time)
        val albumName = findViewById<TextView>(R.id.album_name)
        val releaseYearNumber = findViewById<TextView>(R.id.release_year_number)
        val genre = findViewById<TextView>(R.id.genre_name)
        val country = findViewById<TextView>(R.id.country_name)
        val cover = findViewById<ImageView>(R.id.cover)
        playPauseButton = findViewById(R.id.play_pause_button)

        fun setInfo(info: String): String {
            if (info.isNotEmpty()) {
                return info
            } else {
                return "Error :("
            }
        }

        fun getCoverArtwork() = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        songName.text = setInfo(track.trackName)
        executor.text = setInfo(track.artistName)
        duration.text =
            setInfo(SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis))
        albumName.text = setInfo(track.collectionName)
        releaseYearNumber.text = setInfo(track.releaseDate).take(4)
        genre.text = setInfo(track.primaryGenreName)
        country.text = setInfo(track.country)
        url = setInfo(track.previewUrl)

        Glide.with(cover)
            .load(getCoverArtwork())
            .placeholder(R.drawable.player_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(cover)

        preparePlayer()
        playerBack.setOnClickListener() {
            finish()
        }

        addButton.setOnClickListener() { }

        playPauseButton.setOnClickListener() {
            playbackControl()
        }

        likeButton.setOnClickListener() { }

    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playPauseButton.isEnabled = true
            playerState = STATE_PREPARED
            playerTime.text = TIME_DEF
        }
        mediaPlayer.setOnCompletionListener {
            playPauseButton.setImageDrawable(getDrawable(R.drawable.button_play))
            playerState = STATE_PREPARED
            playerTime.text = TIME_END
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playPauseButton.setImageDrawable(getDrawable(R.drawable.button_pause))
        playerState = STATE_PLAYING
        mainThreadHandler?.post { timerSong }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playPauseButton.setImageDrawable(getDrawable(R.drawable.button_play))
        playerState = STATE_PAUSED

    }

    private fun playbackControl() {
        mainThreadHandler?.post{timerSong.run()}
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }


    private val timerSong = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {
                // Обновляем время в основном потоке
                val totalDuration = mediaPlayer.duration
                val currentPosition = mediaPlayer.currentPosition
                val totalTime = totalDuration-currentPosition
                val currentTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(totalTime)
                playerTime.text = currentTime

                // Планируем следующее обновление через TIME_UPDATE миллисекунд
                mainThreadHandler?.postDelayed(this, TIME_UPDATE)
            }
            if (playerState == STATE_PAUSED){
                mainThreadHandler?.removeCallbacks(this)
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val TIME_UPDATE = 500L
        private const val TIME_DEF = "00:30"
        private const val TIME_END = "00:00"
    }

}