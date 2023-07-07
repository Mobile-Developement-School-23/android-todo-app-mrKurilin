package com.example.todoapp.domain.model

enum class ToDoItemImportance(val value: Int) { LOW(0),

    BASIC(1),

    IMPORTANT(2);

    override fun toString(): String = when (this) {
        LOW -> {
            "low"
        }

        BASIC -> {
            "basic"
        }

        IMPORTANT -> {
            "important"
        }
    }

    companion object {

        fun fromValue(value: Int): ToDoItemImportance {
            return ToDoItemImportance.values().firstOrNull { it.value == value }!!
        }

        fun fromString(value: String): ToDoItemImportance = when (value) {
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