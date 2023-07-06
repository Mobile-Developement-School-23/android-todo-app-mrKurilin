package com.example.todoapp.presentation.todolist.model

import com.example.todoapp.domain.model.ToDoItem
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * Responsible for mapping a domain-level ToDoItem object to a UI-specific ToDoListItemUIModel.
 */
class ToDoListItemUIMapper @Inject constructor() {

    fun map(todoItem: ToDoItem): ToDoListItemUIModel {
        val deadLineDate = if (todoItem.deadLineDate == null) {
            null
        } else {
            SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(todoItem.deadLineDate)
        }
        return ToDoListItemUIModel(
            id = todoItem.id,
            text = todoItem.text,
            isDone = todoItem.isDone,
            importance = todoItem.priority,
            deadLineDate = deadLineDate,
        )
    }
}