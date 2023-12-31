package com.example.todoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.data.local.model.ToDoItemLocal
import com.example.todoapp.data.local.model.ToDoItemLocal.Companion.DEADLINE_EPOCH_DAY_COLUMN_NAME
import com.example.todoapp.data.local.model.ToDoItemLocal.Companion.ID_COLUMN_NAME
import com.example.todoapp.data.local.model.ToDoItemLocal.Companion.TABLE_NAME
import com.example.todoapp.data.local.model.ToDoItemLocal.Companion.TO_DO_ITEM_ACTION_COLUMN_NAME
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for performing database operations on [ToDoItemLocal] in the [ToDoItemLocalDatabase]
 */
@Dao
interface ToDoItemLocalDao {

    @Query("SELECT * FROM $TABLE_NAME WHERE $TO_DO_ITEM_ACTION_COLUMN_NAME IS NOT 'DELETE'")
    fun getToDoItemLocalListFlow(): Flow<List<ToDoItemLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoItemLocal(toDoItemLocal: ToDoItemLocal)

    @Update
    suspend fun updateToDoItemLocal(toDoItemLocal: ToDoItemLocal)

    @Query("DELETE FROM $TABLE_NAME WHERE $ID_COLUMN_NAME = :toDoItemId")
    suspend fun deleteToDoItemById(toDoItemId: String)

    @Query("SELECT * FROM $TABLE_NAME WHERE $TO_DO_ITEM_ACTION_COLUMN_NAME IS NOT NULL")
    suspend fun toDoItemLocalWithRemoteActionList(): List<ToDoItemLocal>

    @Query(
        "SELECT * FROM $TABLE_NAME WHERE " +
                "$TO_DO_ITEM_ACTION_COLUMN_NAME IS NOT 'DELETE' " +
                "AND " +
                "$TO_DO_ITEM_ACTION_COLUMN_NAME IS NOT 'ADD'"
    )
    suspend fun noRemoteActionsToDoItemLocalList(): List<ToDoItemLocal>

    @Query("SELECT $ID_COLUMN_NAME FROM $TABLE_NAME")
    fun getToDoItemIdList(): List<String>

    @Query("SELECT * FROM $TABLE_NAME WHERE $ID_COLUMN_NAME = :toDoItemId")
    suspend fun getToDoItemLocalById(toDoItemId: String): ToDoItemLocal

    @Query("SELECT * FROM $TABLE_NAME WHERE $DEADLINE_EPOCH_DAY_COLUMN_NAME = :epochDay")
    fun getCurrentDeadLineToDoItems(epochDay: Long): List<ToDoItemLocal>
}