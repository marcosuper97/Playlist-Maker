package com.example.playlistmaker.ui.library.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.example.playlistmaker.ui.library.view_model_fragments.LibraryFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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