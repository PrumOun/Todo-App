package com.example.todotasks.ui.topbar

import android.widget.Button
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todotasks.TaskDelegate

@Composable
fun MyTopBar(
    taskDelegate: TaskDelegate
) {
    Box(
        modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 12.dp)
    ) {
        Text(
            text = "Tasks",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
        Button(
            onClick = {
            taskDelegate.requestAddNewCollection()
        }) {
            Text("+ New List")
        }
    }
}