package com.example.todotasks.ui.pagertab

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todotasks.TaskDelegate
import com.example.todotasks.ui.pagertab.state.TaskUiState
import androidx.compose.runtime.key
import androidx.compose.ui.util.fastForEachIndexed

@Composable
fun CompletedTaskListSection(
    completedTaskList: List<TaskUiState>,
    taskDelegate: TaskDelegate
) {
    AnimatedVisibility(completedTaskList.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Color.Black.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 12.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            completedTaskList.fastForEachIndexed { _, taskUiState ->
                key(taskUiState.id){
                    TaskItemLayout(taskUiState, taskDelegate)
                }
            }
        }
    }
}