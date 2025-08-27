package com.example.todotasks.ui.pagertab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todotasks.TaskDelegate
import com.example.todotasks.ui.pagertab.state.TaskUiState

@Composable
fun TaskItemLayout(
    state: TaskUiState,
    taskDelegate: TaskDelegate
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = state.isCompleted,
            onCheckedChange = {
                taskDelegate.onTaskCompleteClick(state)
            }
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1.0f).wrapContentHeight()
        ) {
            Text(
                text = state.content,
                modifier = Modifier.padding(horizontal = 4.dp),
                textDecoration = TextDecoration.LineThrough.takeIf { state.isCompleted }
            )
            if (state.isCompleted){
                Text(
                    text = "Completed at: ${state.stringUpdatedAt}",
                    modifier = Modifier.padding(horizontal = 4.dp),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                )
            }
        }
        if(!state.isCompleted){
            Text(if (state.isFavorite) "👍" else "👎", modifier = Modifier
                .padding(6.dp)
                .clickable {
                    taskDelegate.onTaskFavoriteClick(state)
                })
        }
    }
}

@Preview
@Composable
fun TaskItemLayoutPreview() {
    TaskItemLayout(
        TaskUiState(
            content = "Task 1",
            isCompleted = false,
            isFavorite = true,
            collectionId = 1,
            id = 1,
            updatedAt = 0L,
            stringUpdatedAt = "2025-02-22"
        ),
        taskDelegate = object : TaskDelegate {}
    )
}