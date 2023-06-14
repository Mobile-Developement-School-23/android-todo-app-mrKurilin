package com.example.todoapp.presentation.to_do_list.model

import com.example.todoapp.R
import com.example.todoapp.domain.model.ToDoItem
import com.example.todoapp.domain.model.ToDoItemPriority
import com.example.todoapp.presentation.to_do_item_entry.model.ToDoItemUIModel

class ToDoItemUIMapper {

    fun toToDoItemUIModel(todoItem: ToDoItem): ToDoItemUIModel {
        val priorityStringId = when (todoItem.priority) {
            ToDoItemPriority.LOW -> {
                R.string.low
            }

            ToDoItemPriority.NORMAL -> {
                R.string.no
            }

            ToDoItemPriority.HIGH -> {
                R.string.high
            }
        }

        return ToDoItemUIModel(
            text = todoItem.text,
            priorityStringId = priorityStringId,
            deadLineDate = todoItem.deadLineDate?.time
        )
    }
}