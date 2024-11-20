package com.example.playlistmaker

import android.content.Context
import android.provider.Settings.Global.getString


fun getMockTracks(context: Context): ArrayList<Track> {
    return ArrayList<Track>().apply {
        add(
            Track(
                context.getString(R.string.track_one_name),
                context.getString(R.string.track_one_executor),
                context.getString(R.string.track_one_duration),
                context.getString(R.string.track_one_cover)
            )
        )
        add(
            Track(
                context.getString(R.string.track_two_name),
                context.getString(R.string.track_two_executor),
                context.getString(R.string.track_two_duration),
                context.getString(R.string.track_two_cover)
            )
        )
        add(
            Track(
                context.getString(R.string.track_three_name),
                context.getString(R.string.track_three_executor),
                context.getString(R.string.track_three_duration),
                context.getString(R.string.track_three_cover)
            )
        )
        add(
            Track(
                context.getString(R.string.track_four_name),
                context.getString(R.string.track_four_executor),
                context.getString(R.string.track_four_duration),
                context.getString(R.string.track_four_cover)
            )
        )
        add(
            Track(
                context.getString(R.string.track_five_name),
                context.getString(R.string.track_five_executor),
                context.getString(R.string.track_five_duration),
                context.getString(R.string.track_five_cover)
            )
        )
    }
}