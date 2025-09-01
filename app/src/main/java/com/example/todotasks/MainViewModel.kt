package com.example.todotasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.repository.TaskRepo
import com.example.todotasks.ui.pagertab.state.TabUiState
import com.example.todotasks.ui.pagertab.state.TaskGroupUiState
import com.example.todotasks.ui.pagertab.state.TaskPageUiState
import com.example.todotasks.ui.pagertab.state.TaskUiState
import com.example.todotasks.ui.pagertab.state.millisToDateString
import com.example.todotasks.ui.pagertab.state.toTabUiState
import com.example.todotasks.ui.pagertab.state.toTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

const val ID_ADD_NEW_LIST = -999L
const val ID_FAVORITE_LIST = -1000L

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo
): ViewModel(), TaskDelegate {
    private val _eventFlow: MutableSharedFlow<MainEvent> = MutableSharedFlow()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _listTabGroup: MutableStateFlow<List<TaskGroupUiState>> = MutableStateFlow(emptyList())
    val listTabGroup = _listTabGroup.map {
        listOf(
            TaskGroupUiState(
                tab = TabUiState(ID_FAVORITE_LIST, "⭐"),
                page = TaskPageUiState(
                    mutableListOf<TaskUiState>().apply {
                        it.forEach { tab ->
                            addAll(tab.page.activeTaskList.filter { task -> task.isFavorite })
                        }
                    }.sortedByDescending { task -> task.updatedAt },
                    emptyList()
                )
            )
        ) + it + TaskGroupUiState(
            tab = TabUiState(ID_ADD_NEW_LIST, "＋ New List"),
            page = TaskPageUiState(emptyList(), emptyList())
        )
    }

    private var _currentSelectedCollectionId: Long = -1L

    init {
        viewModelScope.launch {
            val listTasksCollection = taskRepo.getAllTaskCollections()
            val listTabGroupUiState = listTasksCollection.ifEmpty {
                taskRepo.addTaskCollection("My Tasks")?.let { collection ->
                    val id = collection.id
                    taskRepo.addTask("Task 1", id)
                    taskRepo.addTask("Task 2", id)
                    taskRepo.addTask("Task 3", id)
                    taskRepo.addTask("Task 4", id)
                    taskRepo.addTask("Task 5", id)
                }
                taskRepo.getAllTaskCollections()
            }.map { taskCollection ->
                val collectionId = taskCollection.id
                val listTaskUiState = taskRepo.getTaskByCollectionId(collectionId).map { taskEntity ->
                    taskEntity.toTaskUiState()
                }
                val tabUiState = taskCollection.toTabUiState()
                TaskGroupUiState(tabUiState, TaskPageUiState(
                    activeTaskList = listTaskUiState.filter { !it.isCompleted }.sortedByDescending { it.updatedAt },
                    completedTaskList = listTaskUiState.filter { it.isCompleted }.sortedByDescending { it.updatedAt }
                ))
            }
            _listTabGroup.value = listTabGroupUiState
        }
    }

    override fun onTaskCompleteClick(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTaskUiState = taskUiState.copy(isCompleted = !taskUiState.isCompleted)
            if (!taskRepo.updateTaskCompleted(taskUiState.id, taskUiState.isCompleted)) {
                Log.e("MainViewModel", "Failed to update task completed")
                return@launch
            }
            val newTabGroup = _listTabGroup.value.map { tabGroup ->
                val allTasks = tabGroup.page.activeTaskList + tabGroup.page.completedTaskList

                val refreshedTasks = allTasks.map { task ->
                    if(task.id == taskUiState.id){
                        val newUpdatedAt = Calendar.getInstance().timeInMillis
                        newTaskUiState.copy(
                            updatedAt = newUpdatedAt,
                            stringUpdatedAt = newUpdatedAt.millisToDateString()
                        )
                    }else task
                }.sortedByDescending { it.updatedAt }

                tabGroup.copy(
                    page = tabGroup.page.copy(
                        activeTaskList = refreshedTasks.filter { !it.isCompleted }.sortedByDescending { it.updatedAt },
                        completedTaskList = refreshedTasks.filter { it.isCompleted }.sortedByDescending { it.updatedAt }
                    )
                )
            }
            _listTabGroup.value = newTabGroup
        }
    }

    override fun onTaskFavoriteClick(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTaskUiState = taskUiState.copy(isFavorite = !taskUiState.isFavorite)
            if (!taskRepo.updateTaskFavorite(newTaskUiState.id, newTaskUiState.isFavorite)) {
                Log.e("MainViewModel", "Failed to update task favorite")
                return@launch
            }
            val newTabGroup = _listTabGroup.value.map { tabGroup ->
                val allTasks = tabGroup.page.activeTaskList + tabGroup.page.completedTaskList

                val refreshedTasks = allTasks.map { task ->
                    if(task.id == taskUiState.id) newTaskUiState.copy(
                        updatedAt = Calendar.getInstance().timeInMillis,
                        stringUpdatedAt = Calendar.getInstance().time.toString()
                    ) else task
                }.sortedByDescending { it.updatedAt }

                tabGroup.copy(
                    page = tabGroup.page.copy(
                        activeTaskList = refreshedTasks.filter { !it.isCompleted },
                        completedTaskList = refreshedTasks.filter { it.isCompleted }
                    )
                )
            }
            _listTabGroup.value = newTabGroup
        }
    }

    override fun addNewTaskToCurrentCollection(content: String) {
        viewModelScope.launch {
            // 1. Get current tab or return early if not found
            val currentTab = _listTabGroup.value.firstOrNull { it.tab.id == _currentSelectedCollectionId }
                ?: return@launch

            val collectionId = currentTab.tab.id
            if (collectionId <= 0) return@launch

            // 2. Add task to repository or return early if failed
            val taskEntity = taskRepo.addTask(content, collectionId) ?: return@launch
            val newTaskUiState = taskEntity.toTaskUiState()

            // 3. Update listTabGroup with new task
            val updatedTabs = _listTabGroup.value.map { tabGroup ->
                if (tabGroup.tab.id == collectionId) {
                    val updatedTasks = (tabGroup.page.activeTaskList + newTaskUiState)
                        .sortedByDescending { it.updatedAt }
                    tabGroup.copy(page = tabGroup.page.copy(activeTaskList = updatedTasks))
                } else {
                    tabGroup
                }
            }

            // 4. Push updated state
            _listTabGroup.value = updatedTabs
        }
    }

    override fun updateCurrentCollectionId(collectionId: Long){
        _currentSelectedCollectionId = collectionId
    }

    override fun currentCollectionId(): Long {
        return _currentSelectedCollectionId
    }

    override fun addNewCollection(title: String) {
        viewModelScope.launch {
            taskRepo.addTaskCollection(title)?.let { collection ->
                _listTabGroup.value = _listTabGroup.value + TaskGroupUiState(
                    tab = collection.toTabUiState(),
                    page = TaskPageUiState(
                        activeTaskList = emptyList(),
                        completedTaskList = emptyList()
                    )
                )
            }
        }
    }

    override fun requestAddNewCollection() {
        viewModelScope.launch {
            _eventFlow.emit(MainEvent.RequestAddNewCollection)
        }
    }
}

interface TaskDelegate{
    fun onTaskFavoriteClick(taskUiState: TaskUiState) = Unit
    fun onTaskCompleteClick(taskUiState: TaskUiState) = Unit
    fun addNewTaskToCurrentCollection(content: String) = Unit
    fun updateCurrentCollectionId(collectionId: Long) = Unit
    fun currentCollectionId(): Long = -1L
    fun addNewCollection(title: String) = Unit
    fun requestAddNewCollection() {}
}

sealed class MainEvent{
    data object RequestAddNewCollection: MainEvent()
}