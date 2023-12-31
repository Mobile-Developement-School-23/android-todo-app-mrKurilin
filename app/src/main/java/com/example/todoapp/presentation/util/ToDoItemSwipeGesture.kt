package com.example.todoapp.presentation.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.presentation.todolist.ToDoItemViewHolder

const val ICON_MARGIN = 50
const val BACKGROUND_MARGIN = 10
const val SWIPE_VELOCITY_COEFFICIENT = 5

/**
 * Handles the swipe gestures in a RecyclerView for ToDoItems.
 * Displays delete and done icons during swipe actions and provides the necessary logic to draw
 * the icons and background colors based on the swipe direction and item status.
 */
class ToDoItemSwipeGesture(
    private val deleteToDoItem: (Int) -> Unit,
    private val setDoneToDoItem: (Int) -> Unit,
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

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
        if (dX == 0f && !isCurrentlyActive) {
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, false)
            return
        }

        if (dX > 0) {
            val isDone = viewHolder is ToDoItemViewHolder && viewHolder.isDone()
            drawDoneIcon(canvas, viewHolder.itemView, dX, isDone, recyclerView)
        } else {
            val deleteIcon = ContextCompat.getDrawable(
                recyclerView.context,
                R.drawable.delete,
            )!!
            drawDeleteIcon(canvas, viewHolder.itemView, dX, deleteIcon)
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawDeleteIcon(canvas: Canvas, itemView: View, dX: Float, deleteIcon: Drawable) {
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
            AppCompatResources.getDrawable(recyclerView.context, R.drawable.empty_square)!!
        } else {
            ContextCompat.getDrawable(recyclerView.context, R.drawable.check)!!
        }

        background.color = ContextCompat.getColor(recyclerView.context, R.color.color_green)
        val rightBound = dX.toInt() + BACKGROUND_MARGIN
        background.setBounds(itemView.left, itemView.top, rightBound, itemView.bottom)
        background.draw(canvas)

        val doneIconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val doneIconBottom = doneIconTop + icon.intrinsicHeight
        val doneIconRight = dX.toInt() - ICON_MARGIN
        val doneIconLeft = doneIconRight - icon.intrinsicWidth

        icon.setBounds(doneIconLeft, doneIconTop, doneIconRight, doneIconBottom)
        icon.draw(canvas)
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * SWIPE_VELOCITY_COEFFICIENT
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = when (direction) {
        ItemTouchHelper.LEFT -> {
            deleteToDoItem(viewHolder.adapterPosition)
        }

        ItemTouchHelper.RIGHT -> {
            setDoneToDoItem(viewHolder.adapterPosition)
        }

        else -> {
            //do nothing
        }
    }
}