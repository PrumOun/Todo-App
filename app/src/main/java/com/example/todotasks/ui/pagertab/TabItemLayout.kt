package com.example.todotasks.ui.pagertab

import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.todotasks.ID_ADD_NEW_LIST
import com.example.todotasks.ID_FAVORITE_LIST
import com.example.todotasks.ui.pagertab.state.TabUiState

@Composable
fun TabItemLayout(
    state: TabUiState,
    isSelected: Boolean,
    onTabSelected: () -> Unit
){
    Tab(text = {
        Text(
            state.title, color = when (state.id) {
                ID_FAVORITE_LIST -> {
                    Color.Yellow
                }
                ID_ADD_NEW_LIST -> {
                    Color.Blue
                }
                else -> {
                    Color.Unspecified
                }
            }
        )
    }, selected = isSelected, onClick = onTabSelected)
}