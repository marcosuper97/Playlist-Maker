package com.example.playlistmaker.data

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.data.dto.GsonClient
import com.example.playlistmaker.domain.api.TrackHistoryReformater
import com.example.playlistmaker.domain.api.TracksOnClickListener
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.PlayerActivity

class TrackOnClickListenerImpl(private val context: Context, private val trackHistoryReformater: TrackHistoryReformater) : TracksOnClickListener {
    private val playerIntent: Intent = Intent(context, PlayerActivity::class.java)
    private val cleackDebouncer: ClickDebouncer = ClickDebouncer()


    override fun onItemClick(track: Track) {
        if (cleackDebouncer.clickDebounce()) {
            val putTrack = GsonClient.objectToJson(track)
            playerIntent.putExtra("track", putTrack)
            context.startActivity(playerIntent)
            trackHistoryReformater.execute(track)
        }
    }
}