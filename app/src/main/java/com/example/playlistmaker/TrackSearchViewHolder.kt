package com.example.playlistmaker

import android.graphics.Color
import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackSearchViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val executor: TextView = itemView.findViewById(R.id.executor)
    private val duration: TextView = itemView.findViewById(R.id.duration)
    private val cover: ImageView = itemView.findViewById(R.id.cover)

    fun bind(track: Track) {
        trackName.text = track.trackName
        executor.text = track.artistName
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        Glide.with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.track_placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(cover)
    }

}