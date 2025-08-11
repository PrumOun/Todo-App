package com.example.todotasks.repository

import com.example.todotasks.database.entity.TaskCollection
import com.example.todotasks.database.entity.TaskEntity

interface TaskRepo {
    suspend fun getAllTaskCollections(): List<TaskCollection>
    suspend fun getTaskByCollectionId(collectionId: Int): List<TaskEntity>
    suspend fun addTaskCollection(title: String): TaskCollection?
    suspend fun addTask(title: String, collectionId: Int): TaskEntity?
    suspend fun updateTask(task: TaskEntity): Boolean
    suspend fun updateTaskCollection(taskCollection: TaskCollection): Boolean
}