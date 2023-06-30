package com.example.todoapp.presentation.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.presentation.to_do_list_fragment.ToDoItemViewHolder

abstract class ToDoItemSwipeGesture(
    private val deleteIcon: Drawable,
    private val doneIcon: Drawable,
    private val unDoneIcon: Drawable,
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val background = ColorDrawable()
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        if (dX > 0) {
            val icon = if (viewHolder is ToDoItemViewHolder && viewHolder.isDone()) {
                unDoneIcon
            } else {
                doneIcon
            }

            val intrinsicWidth = icon.intrinsicWidth
            val intrinsicHeight = icon.intrinsicHeight

            val backgroundColor = ContextCompat.getColor(
                recyclerView.context,
                R.color.color_green
            )

            background.color = backgroundColor
            background.setBounds(itemView.left, itemView.top, dX.toInt() + 10, itemView.bottom)
            background.draw(c)

            val doneIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val doneIconBottom = doneIconTop + intrinsicHeight
            val doneIconRight = dX.toInt() - 50
            val doneIconLeft = doneIconRight - intrinsicWidth

            icon.setBounds(doneIconLeft, doneIconTop, doneIconRight, doneIconBottom)
            icon.draw(c)
        } else {
            val intrinsicWidth = deleteIcon.intrinsicWidth
            val intrinsicHeight = deleteIcon.intrinsicHeight
            // Draw the delete background
            val backgroundColor = Color.parseColor("#f44336")
            background.color = backgroundColor
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            background.draw(c)

            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val deleteIconBottom = deleteIconTop + intrinsicHeight
            val deleteIconLeft = dX.toInt() + itemView.width + 50
            val deleteIconRight = deleteIconLeft + intrinsicWidth

            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 5
    }
}