package com.example.todotasks.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todotasks.database.entity.TaskCollection
import com.example.todotasks.database.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCollection(taskCollection: TaskCollection)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM task_collection")
    suspend fun getAllTaskCollections(): List<TaskCollection>
    @Query("SELECT * FROM task WHERE collection_id = :collectionId")
    suspend fun getTasksByCollectionId(collectionId: Int): List<TaskEntity>

    @Query("UPDATE task SET is_favorite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskFavoriteStatus(taskId: Int, isFavorite: Boolean)
    @Query("UPDATE task SET is_completed = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletionStatus(taskId: Int, isCompleted: Boolean)
    @Query("UPDATE task_collection SET title = :title WHERE id = :collectionId")
    suspend fun updateTaskCollectionTitle(collectionId: Int, title: String)
    @Update
    suspend fun updateTask(task: TaskEntity)
    @Update
    suspend fun updateTaskCollection(taskCollection: TaskCollection)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
    @Delete
    suspend fun deleteTaskCollection(taskCollection: TaskCollection)
}