package com.example.todoapp.data.local

import androidx.room.TypeConverter
import com.example.todoapp.data.local.model.ToDoItemAction

class Converters {

    @TypeConverter
    fun toToDoItemAction(value: String?): ToDoItemAction? {
        return if (value == null) {
            null
        } else {
            enumValueOf<ToDoItemAction>(value)
        }
    }

    @TypeConverter
    fun fromToDoItemAction(value: ToDoItemAction?) = value?.name
}