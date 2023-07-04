package com.example.todoapp.presentation.to_do_list_fragment

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.presentation.to_do_list_fragment.model.ToDoListItemUIModel
import com.example.todoapp.presentation.util.GenericDiffUtilCallback
import com.example.todoapp.presentation.util.ToDoItemSwipeGesture

class ToDoItemsAdapter(
    private val deleteToDoItem: (String) -> Unit,
    private val setDoneToDoItem: (String) -> Unit,
    private val editToDoItem: (String) -> Unit,
) : RecyclerView.Adapter<ToDoItemViewHolder>() {

    private var toDoList: List<ToDoListItemUIModel> = listOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val deleteIcon = ContextCompat.getDrawable(
            context,
            R.drawable.delete,
        )!!
        val doneIcon = ContextCompat.getDrawable(
            context,
            R.drawable.check,
        )!!
        val unDoneIcon = ContextCompat.getDrawable(
            context,
            R.drawable.baseline_crop_square_24,
        )!!
        val toDoItemSwipeGesture = object : ToDoItemSwipeGesture(deleteIcon, doneIcon, unDoneIcon) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val toDoItemId = toDoList[viewHolder.adapterPosition].id
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        deleteToDoItem(toDoItemId)
                    }

                    ItemTouchHelper.RIGHT -> {
                        setDoneToDoItem(toDoItemId)
                    }
                }
            }
        }
        val item = ItemTouchHelper(toDoItemSwipeGesture)
        item.attachToRecyclerView(recyclerView)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val viewHolder = ToDoItemViewHolder(
            parent = parent,
            deleteToDoItem = { position ->
                val toDoItemId = toDoList[position].id
                deleteToDoItem(toDoItemId)
            },
            editToDoItem = { position ->
                val toDoItemId = toDoList[position].id
                editToDoItem(toDoItemId)
            },
            setDoneToDoItem = { position ->
                val toDoItemId = toDoList[position].id
                setDoneToDoItem(toDoItemId)
            },
        )

        return viewHolder
    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

    override fun onBindViewHolder(toDoItemViewHolder: ToDoItemViewHolder, position: Int) {
        toDoItemViewHolder.bind(toDoList[position])
    }

    fun setToDoItems(toDoList: List<ToDoListItemUIModel>) {
        val diffResult = DiffUtil.calculateDiff(
            GenericDiffUtilCallback(
                this.toDoList,
                toDoList,
            )
        )
        this.toDoList = toDoList
        diffResult.dispatchUpdatesTo(this)
    }
}