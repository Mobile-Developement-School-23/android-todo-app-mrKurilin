package com.example.todoapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.data.local.model.ToDoItemLocal
import com.example.todoapp.data.local.model.ToDoItemLocal.Companion.ID_COLUMN_NAME
import com.example.todoapp.data.local.model.ToDoItemLocal.Companion.TABLE_NAME
import com.example.todoapp.data.local.model.ToDoItemLocal.Companion.TO_DO_ITEM_ACTION_COLUMN_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoItemLocalDao {

    @Query("SELECT * FROM $TABLE_NAME WHERE $TO_DO_ITEM_ACTION_COLUMN_NAME IS NOT 'DELETE'")
    fun getToDoItemLocalListFlow(): Flow<List<ToDoItemLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToDoItemLocal(toDoItemLocal: ToDoItemLocal)

    @Update
    fun updateToDoItemLocal(toDoItemLocal: ToDoItemLocal)

    @Query("DELETE FROM $TABLE_NAME WHERE $ID_COLUMN_NAME = :toDoItemId")
    fun deleteToDoItemById(toDoItemId: String)

    @Query("SELECT * FROM $TABLE_NAME WHERE $TO_DO_ITEM_ACTION_COLUMN_NAME IS NOT NULL")
    fun getToDoItemsToUpdateRemote(): List<ToDoItemLocal>

    @Query("SELECT * FROM $TABLE_NAME WHERE $TO_DO_ITEM_ACTION_COLUMN_NAME IS NULL")
    fun getToDoItemsWithoutRemoteActions(): List<ToDoItemLocal>

    @Query("SELECT * FROM $TABLE_NAME")
    fun getToDoItemLocalList(): List<ToDoItemLocal>

    @Query("SELECT $ID_COLUMN_NAME FROM $TABLE_NAME")
    fun getToDoItemIdList(): List<String>

    @Query("SELECT * FROM $TABLE_NAME WHERE $ID_COLUMN_NAME = :toDoItemId")
    fun getToDoItemLocalById(toDoItemId: String): ToDoItemLocal
}