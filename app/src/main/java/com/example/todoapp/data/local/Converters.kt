package com.example.todoapp.data.local

import androidx.room.TypeConverter
import com.example.todoapp.data.local.model.ToDoItemLocalRemoteAction

class Converters {

    @TypeConverter
    fun toToDoItemAction(value: String?): ToDoItemLocalRemoteAction? {
        return if (value == null) {
            null
        } else {
            enumValueOf<ToDoItemLocalRemoteAction>(value)
        }
    }

    @TypeConverter
    fun fromToDoItemAction(value: ToDoItemLocalRemoteAction?) = value?.name
}