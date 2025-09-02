package com.example.todotasks.repository

import com.example.todotasks.database.entity.TaskCollection
import com.example.todotasks.database.entity.TaskEntity

interface TaskRepo {
    suspend fun getAllTaskCollections(): List<TaskCollection>
    suspend fun getTaskByCollectionId(collectionId: Long): List<TaskEntity>
    suspend fun addTaskCollection(title: String): TaskCollection?
    suspend fun addTask(content: String, collectionId: Long): TaskEntity?
    suspend fun updateTask(task: TaskEntity): Boolean
    suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean
    suspend fun updateTaskCollection(taskCollection: TaskCollection): Boolean
    suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean
    suspend fun deleteTaskCollectionById(collectionId: Long): Boolean
}