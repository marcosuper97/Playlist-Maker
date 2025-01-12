package com.example.playlistmaker

import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    lateinit var track: Track
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.playerActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val trackJson = intent.getStringExtra("track")
        if (trackJson != null) {
            track = GsonClient.objectFromJson(trackJson)
        }
        val playerBack = findViewById<Toolbar>(R.id.player_back)
        val songName = findViewById<TextView>(R.id.song_name)
        val executor = findViewById<TextView>(R.id.executor)
        val addButton = findViewById<ImageView>(R.id.add_button)
        val playPauseButton = findViewById<ImageView>(R.id.play_pause_button)
        val likeButton = findViewById<ImageView>(R.id.like_button)
        val playerTime = findViewById<TextView>(R.id.time)
        val duration = findViewById<TextView>(R.id.duration_time)
        val albumName = findViewById<TextView>(R.id.album_name)
        val releaseYearNumber = findViewById<TextView>(R.id.release_year_number)
        val genre = findViewById<TextView>(R.id.genre_name)
        val country = findViewById<TextView>(R.id.country_name)
        val cover = findViewById<ImageView>(R.id.cover)

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

        Glide.with(cover)
            .load(getCoverArtwork())
            .placeholder(R.drawable.player_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(cover)

        playerBack.setOnClickListener() {
            finish()
        }

        addButton.setOnClickListener() { }

        playPauseButton.setOnClickListener() { }

        likeButton.setOnClickListener() { }
    }
}