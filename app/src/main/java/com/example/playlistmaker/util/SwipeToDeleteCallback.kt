package com.example.playlistmaker.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.adapters.playlist_fragment.PlaylistTracksAdapter

class SwipeToDeleteCallback(
    private val adapter: PlaylistTracksAdapter,
    private val context: Context
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteIcon = getDrawable(context, R.drawable.trash_can)
    private val background = ColorDrawable(context.getColor(R.color.like_color))

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView

        // Рисуем фон только если есть свайп
        if (dX != 0f) {
            // Для свайпа влево (dX отрицательный) или вправо (dX положительный)
            if (dX > 0) { // Свайп вправо
                background.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.left + dX.toInt(),
                    itemView.bottom
                )
            } else { // Свайп влево
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }
            background.draw(canvas)

            // Рисуем иконку
            deleteIcon?.let {
                val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - it.intrinsicHeight) / 2
                val iconBottom = iconTop + it.intrinsicHeight

                if (dX > 0) { // Свайп вправо
                    val iconLeft = itemView.left + iconMargin
                    val iconRight = iconLeft + it.intrinsicWidth
                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                } else { // Свайп влево
                    val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                }
                it.draw(canvas)
            }
        } else {
            // Если свайпа нет, очищаем фон
            background.setBounds(0, 0, 0, 0)
            background.draw(canvas)
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}