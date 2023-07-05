package com.example.todoapp.presentation.util

import androidx.recyclerview.widget.DiffUtil

/**
 * A generic implementation of DiffUtil.Callback that calculates the differences between two
 * lists of items.
 *
 * @param <T> The type of items in the lists.
 *
 * The differences between the lists are determined based on the provided lambda expressions
 * for item comparison. By default, it compares items for equality using the '==' operator.
 * Custom comparison logic can be defined by providing lambdas for item comparison and
 * content comparison.
 */
class GenericDiffUtilCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val areItemsTheSameProp: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    },
    private val areContentsTheSameProp: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    },
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return areItemsTheSameProp(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return areContentsTheSameProp(oldItem, newItem)
    }
}