package com.example.todotasks.ui.pagertab

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.todotasks.ID_ADD_NEW_LIST
import com.example.todotasks.ID_FAVORITE_LIST
import com.example.todotasks.ui.pagertab.state.TabUiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabItemLayout(
    state: TabUiState,
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    onTabLongPressed: (TabUiState) -> Unit = {}
){
    Tab(selected = isSelected, onClick = onTabSelected) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .combinedClickable(
                    onClick = onTabSelected,
                    onLongClick = { onTabLongPressed(state) }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.title,
                color = when (state.id) {
                    ID_FAVORITE_LIST -> Color.Yellow
                    ID_ADD_NEW_LIST -> Color.Blue
                    else -> Color.Unspecified
                }
            )
        }
    }
}