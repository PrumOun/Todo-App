package com.example.todotasks.ui.pagertab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todotasks.TaskDelegate
import com.example.todotasks.ui.pagertab.items.activeTasksHeader
import com.example.todotasks.ui.pagertab.items.bottomCorner
import com.example.todotasks.ui.pagertab.items.emptyState
import com.example.todotasks.ui.pagertab.items.listTaskItems
import com.example.todotasks.ui.pagertab.items.spacer
import com.example.todotasks.ui.pagertab.items.topCorner
import com.example.todotasks.ui.pagertab.state.TaskGroupUiState
import com.example.todotasks.ui.pagertab.state.TaskPageUiState

@Composable
fun TaskListPage(
    //collectionId: Long,
    state: TaskGroupUiState,
    taskDelegate: TaskDelegate
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //ActiveTaskListSection(collectionId, state.activeTaskList, taskDelegate)
        //CompletedTaskListSection(state.completedTaskList, taskDelegate)

        topCorner()
        activeTasksHeader("header", state, taskDelegate)
        emptyState("empty", state.page)
        listTaskItems("active", state.page.activeTaskList, taskDelegate)
        bottomCorner()
        spacer(24)

        topCorner()
        listTaskItems("completed", state.page.completedTaskList, taskDelegate)
        bottomCorner()
    }
}