package com.example.playlistmaker.ui.adapters.playlists_library

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist

class PlaylistListAdapter(
    private val context: Context,
    private val onTrackClickListener: (Playlist) -> Unit
) :
    RecyclerView.Adapter<PlaylistListViewHolder>() {

    var playlists: List<Playlist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_container, parent, false)
        return PlaylistListViewHolder(view, onTrackClickListener)
    }

    override fun onBindViewHolder(holder: PlaylistListViewHolder, position: Int) {
        holder.bind(
            playlists[position],
            context = context
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}