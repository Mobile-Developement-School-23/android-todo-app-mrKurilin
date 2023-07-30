package com.example.todoapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.data.local.model.ToDoItemLocal.Companion.TABLE_NAME

/**
 * Represents a ToDoItem stored in a local database.
 */
@Entity(tableName = TABLE_NAME)
data class ToDoItemLocal(
    @PrimaryKey @ColumnInfo(name = ID_COLUMN_NAME) val id: String,
    @ColumnInfo(name = TEXT_COLUMN_NAME) val text: String,
    @ColumnInfo(name = IS_DONE_COLUMN_NAME) val isDone: Boolean,
    @ColumnInfo(name = CREATION_DATE_MILLIS_COLUMN_NAME) val creationDateMillis: Long,
    @ColumnInfo(name = EDIT_DATE_MILLIS_COLUMN_NAME) val editDateMillis: Long = creationDateMillis,
    @ColumnInfo(name = IMPORTANCE_COLUMN_NAME) val importance: Int,
    @ColumnInfo(name = DEADLINE_EPOCH_DAY_COLUMN_NAME) val deadLineEpochDay: Long?,
    @ColumnInfo(name = TO_DO_ITEM_ACTION_COLUMN_NAME) val toDoItemLocalRemoteAction: ToDoItemLocalRemoteAction? = null,
) {

    companion object {

        const val TABLE_NAME = "ToDoItemLocal"
        const val ID_COLUMN_NAME = "id"
        const val TEXT_COLUMN_NAME = "text"
        const val IS_DONE_COLUMN_NAME = "is_done"
        const val CREATION_DATE_MILLIS_COLUMN_NAME = "creation_date_millis"
        const val EDIT_DATE_MILLIS_COLUMN_NAME = "edit_date_millis"
        const val IMPORTANCE_COLUMN_NAME = "importance"
        const val DEADLINE_EPOCH_DAY_COLUMN_NAME = "deadline_date_millis"
        const val TO_DO_ITEM_ACTION_COLUMN_NAME = "is_loaded_to_remote"
    }
}