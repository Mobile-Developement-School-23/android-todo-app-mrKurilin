package com.example.todoapp.domain.model

enum class ToDoItemImportance(val value: Int) {
    LOW(0), BASIC(1), IMPORTANT(2);

    companion object {

        fun from(value: Int): ToDoItemImportance {
            return ToDoItemImportance.values().firstOrNull { it.value == value }!!
        }

        fun fromString(value: String): ToDoItemImportance {
            return when (value) {
                "basic" -> {
                    BASIC
                }

                "low" -> {
                    LOW
                }

                "important" -> {
                    IMPORTANT
                }

                else -> {
                    error("Passed illegal value")
                }
            }
        }
    }
}