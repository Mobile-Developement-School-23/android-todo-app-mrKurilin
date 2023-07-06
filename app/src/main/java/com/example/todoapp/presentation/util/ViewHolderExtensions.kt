package com.example.todoapp.presentation.util

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.getColorStateList(@ColorRes id: Int): ColorStateList? {
    return ContextCompat.getColorStateList(itemView.context, id)
}

fun RecyclerView.ViewHolder.getDrawable(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(itemView.context, id)
}