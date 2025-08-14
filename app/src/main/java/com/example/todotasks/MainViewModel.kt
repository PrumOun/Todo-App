package com.example.todotasks

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todotasks.repository.TaskRepo
import com.example.todotasks.ui.pagertab.state.TabUiState
import com.example.todotasks.ui.pagertab.state.TaskGroupUiState
import com.example.todotasks.ui.pagertab.state.TaskPageUiState
import com.example.todotasks.ui.pagertab.state.TaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo
): ViewModel(), TaskDelegate {
    private val _listTabGroup: MutableStateFlow<List<TaskGroupUiState>> = MutableStateFlow(emptyList())
    val listTabGroup = _listTabGroup.asStateFlow()

    init {
        _listTabGroup.value = listOf(
            TaskGroupUiState(
                tab = TabUiState(1, "First Tab"),
                page = TaskPageUiState(
                    listOf(
                        TaskUiState(
                            id = 1,
                            content = "Task 1",
                            collectionId = 1
                        ),
                        TaskUiState(
                            id = 2,
                            content = "Task 2",
                            collectionId = 1
                        ),
                        TaskUiState(
                            id = 3,
                            content = "Task 3",
                            collectionId = 1,
                            isFavorite = true
                        )
                    ), listOf()
                )
            ),
            TaskGroupUiState(
                tab = TabUiState(2, "Second Tab"),
                page = TaskPageUiState(
                    listOf(), listOf()
                )
            ),
        )
    }

    override fun onTaskCompleteClick(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTaskUiState = taskUiState.copy(isCompleted = !taskUiState.isCompleted)
            val newTabGroup = listTabGroup.value.map { tabGroup ->
                val allTasks = tabGroup.page.activeTaskList + tabGroup.page.completedTaskList

                val refreshedTasks = allTasks.map { task ->
                    if(task.id == taskUiState.id) newTaskUiState else task
                }

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

    override fun onTaskFavoriteClick(taskUiState: TaskUiState) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTaskUiState = taskUiState.copy(isFavorite = !taskUiState.isFavorite)
            val newTabGroup = listTabGroup.value.map { tabGroup ->
                val allTasks = tabGroup.page.activeTaskList + tabGroup.page.completedTaskList

                val refreshedTasks = allTasks.map { task ->
                    if(task.id == taskUiState.id) newTaskUiState else task
                }

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
}

interface TaskDelegate{
    fun onTaskFavoriteClick(taskUiState: TaskUiState)
    fun onTaskCompleteClick(taskUiState: TaskUiState)
}