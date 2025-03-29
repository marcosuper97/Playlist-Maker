package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.ui.library.view_model_activity.LibraryActivityViewModel
import com.example.playlistmaker.ui.library.view_model_fragments.LibraryFragmentViewModel
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private val viewModel: LibraryFragmentViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = LibraryFragment().apply {
            arguments = bundleOf()
        }
    }

}