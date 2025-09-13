package com.example.todotasks.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todotasks.database.entity.TaskCollection
import com.example.todotasks.database.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCollection(taskCollection: TaskCollection): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Query("SELECT * FROM task_collection")
    suspend fun getAllTaskCollections(): List<TaskCollection>
    @Query("SELECT * FROM task WHERE collection_id = :collectionId")
    suspend fun getTasksByCollectionId(collectionId: Long): List<TaskEntity>

    @Query("UPDATE task SET is_favorite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskFavoriteStatus(taskId: Long, isFavorite: Boolean) : Int
    @Query("UPDATE task SET is_completed = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletionStatus(taskId: Long?, isCompleted: Boolean): Int
    @Query("UPDATE task_collection SET title = :title WHERE id = :collectionId")
    suspend fun updateTaskCollectionTitle(collectionId: Long, title: String): Int

    @Update
    suspend fun updateTask(task: TaskEntity)
    @Update
    suspend fun updateTaskCollection(taskCollection: TaskCollection): Int
    @Query("UPDATE task_collection SET sort_type = :sortType WHERE id = :collectionId")
    suspend fun updateCollectionSortType(collectionId: Long, sortType: Int): Int

    @Delete
    suspend fun deleteTask(task: TaskEntity)
    @Query("DELETE FROM task WHERE collection_id = :collectionId")
    suspend fun deleteTaskById(collectionId: Long): Int
    @Query("DELETE FROM task_collection WHERE id = :collectionId")
    suspend fun deleteTaskCollectionById(collectionId: Long): Int
    @Transaction
    suspend fun deleteTaskCollectionWithTasks(collectionId: Long){
        deleteTaskById(collectionId)
        deleteTaskCollectionById(collectionId)
    }
}