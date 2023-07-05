package com.example.todoapp.presentation.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.presentation.todolist.ToDoItemViewHolder

const val ICON_MARGIN = 50
const val BACKGROUND_MARGIN = 10
const val SWIPE_VELOCITY_COEFFICIENT = 5

abstract class ToDoItemSwipeGesture(
    private val deleteIcon: Drawable,
    private val doneIcon: Drawable,
    private val unDoneIcon: Drawable,
) : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private val background = ColorDrawable()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
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
        val isCanceled = dX == 0f && !isCurrentlyActive
        if (isCanceled) {
            clearCanvas(
                canvas,
                viewHolder.itemView.right + dX,
                viewHolder.itemView.top.toFloat(),
                viewHolder.itemView.right.toFloat(),
                viewHolder.itemView.bottom.toFloat()
            )
            super.onChildDraw(
                canvas,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
            return
        }

        if (dX > 0) {
            drawDoneIcon(
                canvas = canvas,
                itemView = viewHolder.itemView,
                dX = dX,
                isDone = viewHolder is ToDoItemViewHolder && viewHolder.isDone(),
                recyclerView = recyclerView,
            )
        } else {
            drawDeleteIcon(canvas, viewHolder.itemView, dX)
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawDeleteIcon(canvas: Canvas, itemView: View, dX: Float) {
        val intrinsicWidth = deleteIcon.intrinsicWidth
        val intrinsicHeight = deleteIcon.intrinsicHeight
        val backgroundColor = Color.parseColor("#f44336")
        background.color = backgroundColor
        background.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        background.draw(canvas)

        val deleteIconTop = itemView.top + (itemView.height - intrinsicHeight) / 2
        val deleteIconBottom = deleteIconTop + intrinsicHeight
        val deleteIconLeft = dX.toInt() + itemView.width + ICON_MARGIN
        val deleteIconRight = deleteIconLeft + intrinsicWidth

        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(canvas)
    }

    private fun drawDoneIcon(
        canvas: Canvas,
        itemView: View,
        dX: Float,
        isDone: Boolean,
        recyclerView: RecyclerView
    ) {
        val icon = if (isDone) {
            unDoneIcon
        } else {
            doneIcon
        }

        val backgroundColor = ContextCompat.getColor(
            recyclerView.context,
            R.color.color_green
        )

        background.color = backgroundColor
        background.setBounds(
            itemView.left,
            itemView.top,
            dX.toInt() + BACKGROUND_MARGIN,
            itemView.bottom
        )
        background.draw(canvas)

        val doneIconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val doneIconBottom = doneIconTop + icon.intrinsicHeight
        val doneIconRight = dX.toInt() - ICON_MARGIN
        val doneIconLeft = doneIconRight - icon.intrinsicWidth

        icon.setBounds(doneIconLeft, doneIconTop, doneIconRight, doneIconBottom)
        icon.draw(canvas)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * SWIPE_VELOCITY_COEFFICIENT
    }
}