package com.example.todoapp.domain.model

enum class ToDoItemPriority(val value: Int) {
    LOW(0), NORMAL(1), HIGH(2);

    companion object {

        fun from(value: Int): ToDoItemPriority {
            return ToDoItemPriority.values().firstOrNull { it.value == value }!!
        }
    }
}