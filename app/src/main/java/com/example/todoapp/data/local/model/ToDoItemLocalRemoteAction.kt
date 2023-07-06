package com.example.todoapp.data.local.model

/**
 * Represents the different actions that should be update on remote with a [ToDoItemLocal]
 */
enum class ToDoItemLocalRemoteAction {
    UPDATE,
    DELETE,
    ADD,
}