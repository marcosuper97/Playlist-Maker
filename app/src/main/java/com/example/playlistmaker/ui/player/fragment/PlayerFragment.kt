package com.example.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.player.PlayerStateUi
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by lazy {
        getViewModel { parametersOf(arguments?.getString(TRACK_TAG)) }
    }

    override fun onDestroy() {
        findNavController().popBackStack(R.id.searchFragment,false)
        _binding = null
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.playerStateUi.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlayerStateUi.Pause -> {
                    showUi(state.track)
                    binding.playPauseButton.setImageDrawable(requireContext().getDrawable(R.drawable.button_play))
                }

                is PlayerStateUi.Play -> {
                    showUi(state.track)
                    binding.time.text = state.time
                    binding.playPauseButton.setImageDrawable(requireContext().getDrawable(R.drawable.button_pause))
                }

                is PlayerStateUi.ReadyToPlay -> {
                    showUi(state.track)
                    binding.playPauseButton.setImageDrawable(requireContext().getDrawable(R.drawable.button_play))
                    binding.time.text = TIME_DEF
                }
            }
        }

        binding.playerBack.setOnClickListener {
            onDestroy()
        }

        binding.playPauseButton.setOnClickListener {
            viewModel.onClickPlayMusic()
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.activityOnPause()
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
        private const val TRACK_TAG = "track"

        fun createArgs(track: String): Bundle = bundleOf(
            TRACK_TAG to track
        )
    }

}