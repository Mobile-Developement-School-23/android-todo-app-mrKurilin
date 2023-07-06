package com.example.todoapp.presentation.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ToDoItemViewHolderBinding
import com.example.todoapp.presentation.todolist.model.ToDoListItemUIModel
import com.example.todoapp.presentation.util.GenericDiffUtilCallback
import com.example.todoapp.presentation.util.ToDoItemSwipeGesture

class ToDoItemsAdapter(
    private val deleteToDoItem: (String) -> Unit,
    private val setDoneToDoItem: (String) -> Unit,
    private val editToDoItem: (String) -> Unit,
) : RecyclerView.Adapter<ToDoItemViewHolder>() {

    private var toDoList: List<ToDoListItemUIModel> = listOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val toDoItemSwipeGesture = ToDoItemSwipeGesture(
            deleteToDoItem = { id ->
                deleteToDoItem(toDoList[id].id)
            },
            setDoneToDoItem = { id ->
                setDoneToDoItem(toDoList[id].id)
            }
        )
        ItemTouchHelper(toDoItemSwipeGesture).attachToRecyclerView(recyclerView)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ToDoItemViewHolder(
        binding = ToDoItemViewHolderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
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

    override fun getItemCount(): Int {
        return toDoList.size
    }

    override fun onBindViewHolder(toDoItemViewHolder: ToDoItemViewHolder, position: Int) {
        toDoItemViewHolder.bind(toDoList[position])
    }

    fun setToDoItems(toDoList: List<ToDoListItemUIModel>) {
        val diffResult = DiffUtil.calculateDiff(
            GenericDiffUtilCallback(this.toDoList, toDoList)
        )
        this.toDoList = toDoList
        diffResult.dispatchUpdatesTo(this)
    }
}