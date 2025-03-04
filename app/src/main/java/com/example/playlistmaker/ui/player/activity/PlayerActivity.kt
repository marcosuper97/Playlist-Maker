package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerStateUi
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.playerActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(intent.getStringExtra("track").toString())
        )[PlayerViewModel::class.java]

        viewModel.timer.observe(this) { time ->
            binding.time.text = time
        }

        viewModel.playerStateUi.observe(this) { state ->
            when (state) {
                is PlayerStateUi.Pause -> {
                    showUi(state.track)
                    binding.playPauseButton.setImageDrawable(getDrawable(R.drawable.button_play))
                }

                is PlayerStateUi.Play -> {
                    showUi(state.track)
                    binding.playPauseButton.setImageDrawable(getDrawable(R.drawable.button_pause))
                }

                is PlayerStateUi.ReadyToPlay -> {
                    showUi(state.track)
                    binding.playPauseButton.setImageDrawable(getDrawable(R.drawable.button_play))
                    binding.time.text = TIME_DEF
                }
            }
        }

        binding.playerBack.setOnClickListener {
            finish()
        }

        binding.playPauseButton.setOnClickListener {
                viewModel.onClickPlayMusic()
        }
    }

    private fun showUi(track: Track) {
        binding.songName.text = track.trackName
        binding.executor.text = track.artistName
        binding.durationTime.text =
            SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(track.trackTimeMillis)
        binding.albumName.text = track.collectionName
        binding.releaseYearNumber.text = track.releaseDate.take(4)
        binding.genreName.text = track.primaryGenreName
        binding.countryName.text = track.country
        Glide.with(binding.cover)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.player_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.cover)
    }

    companion object {
        private const val TIME_DEF = "00:30"
    }
}