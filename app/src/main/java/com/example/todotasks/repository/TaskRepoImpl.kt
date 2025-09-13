package com.example.todotasks.repository

import android.icu.util.Calendar
import com.example.todotasks.database.dao.TaskDao
import com.example.todotasks.database.entity.SortType
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
    override suspend fun getTaskByCollectionId(collectionId: Long): List<TaskEntity> = withContext(Dispatchers.IO) {
        taskDao.getTasksByCollectionId(collectionId)
    }
    override suspend fun addTaskCollection(title: String): TaskCollection? {
        val now = Calendar.getInstance().timeInMillis
        val taskCollection = TaskCollection(title = title, updateAt = now, sortType = SortType.CREATED_DATE.value)
        val id = taskDao.insertTaskCollection(taskCollection)
        return if(id > 0) {
            taskCollection.copy(id = id)
        } else {
            null
        }
    }
    override suspend fun addTask(content: String, collectionId: Long): TaskEntity? = withContext(Dispatchers.IO) {
        val task = TaskEntity(content = content, collectionId = collectionId, isCompleted = false, isFavorite = false, updatedAt = Calendar.getInstance().timeInMillis)
        val id = taskDao.insertTask(task)
        return@withContext if(id > 0) {
            task.copy(id = id)
        } else {
            null
        }
    }
    override suspend fun updateTask(task: TaskEntity): Boolean = withContext(Dispatchers.IO) {
        taskDao.updateTask(task)
        return@withContext true
    }

    override suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean = withContext(Dispatchers.IO){
        taskDao.updateTaskCompletionStatus(taskId, isCompleted) > 0
    }

    override suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean = withContext(Dispatchers.IO) {
        taskDao.updateTaskFavoriteStatus(taskId, isFavorite) > 0
    }

    override suspend fun updateTaskCollection(taskCollection: TaskCollection): Boolean = withContext(Dispatchers.IO) {
        taskDao.updateTaskCollection(taskCollection) > 0
    }

    override suspend fun deleteTaskCollectionById(collectionId: Long): Boolean {
        taskDao.deleteTaskCollectionWithTasks(collectionId)
        return true
    }

    override suspend fun updateCollectionSortType(collectionId: Long, sortType: SortType): Boolean {
        return withContext(Dispatchers.IO) {
            taskDao.updateCollectionSortType(collectionId, sortType = sortType.value) > 0
        }
    }

    override suspend fun updateTasksCollectionTitle(collectionId: Long, newTitle: String): Boolean = withContext(Dispatchers.IO) {
        taskDao.updateTaskCollectionTitle(collectionId, newTitle) > 0
    }
}