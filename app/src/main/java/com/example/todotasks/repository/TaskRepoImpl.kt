package com.example.todotasks.repository

import android.icu.util.Calendar
import com.example.todotasks.database.dao.TaskDao
import com.example.todotasks.database.entity.TaskCollection
import com.example.todotasks.database.entity.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskRepoImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepo {
    override suspend fun getAllTaskCollections(): List<TaskCollection> = withContext(Dispatchers.IO){
        taskDao.getAllTaskCollections()
    }
    override suspend fun getTaskByCollectionId(collectionId: Int): List<TaskEntity> = withContext(Dispatchers.IO) {
        taskDao.getTasksByCollectionId(collectionId)
    }
    override suspend fun addTaskCollection(title: String): TaskCollection? {
        val taskCollection = TaskCollection(title = title, updateAt = Calendar.getInstance().timeInMillis)
        val id = taskDao.insertTaskCollection(taskCollection)
        return if(id > 0) {
            taskCollection.copy(id = id)
        } else {
            null
        }
    }
    override suspend fun addTask(title: String, collectionId: Int): TaskEntity? {
        val task = TaskEntity(title = title, collectionId = collectionId, isCompleted = false, isFavorite = false, updatedAt = Calendar.getInstance().timeInMillis)
        val id = taskDao.insertTask(task)
        return if(id > 0) {
            task.copy(id = id)
        } else {
            null
        }
    }
    override suspend fun updateTask(task: TaskEntity): Boolean = withContext(Dispatchers.IO) {
        taskDao.updateTask(task)
        return@withContext true
    }
    override suspend fun updateTaskCollection(taskCollection: TaskCollection): Boolean = withContext(Dispatchers.IO) {
        taskDao.updateTaskCollection(taskCollection)
        return@withContext true
    }
}