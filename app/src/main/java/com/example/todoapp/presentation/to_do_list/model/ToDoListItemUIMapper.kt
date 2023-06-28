package com.example.todoapp.presentation.to_do_list.model

import com.example.todoapp.domain.model.ToDoItem
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

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
            priority = todoItem.priority,
            deadLineDate = deadLineDate,
        )
    }
}