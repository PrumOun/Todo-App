package com.example.todotasks.ui.pagertab

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.todotasks.TaskDelegate
import com.example.todotasks.ui.pagertab.state.TabUiState
import com.example.todotasks.ui.pagertab.state.TaskGroupUiState
import com.example.todotasks.ui.pagertab.state.TaskPageUiState
import com.example.todotasks.ui.pagertab.state.TaskUiState
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun PagerTabLayout(
    state: List<TaskGroupUiState>,
    taskDelegate: TaskDelegate
) {
    var pageCount by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState { pageCount }
    val scope = rememberCoroutineScope()

    pageCount = state.size
    MyTabRowLayout(
        selectedTabIndex = pagerState.currentPage,
        listTabs = state.map { it.tab },
        onTabSelected = { index ->
            scope.launch {
                pagerState.scrollToPage(index)
            }
        }
    )
    HorizontalPager(
        state = pagerState,
        key = { it },
        beyondViewportPageCount = 2
    ){ pageIndex ->
        TaskListPage(
            state = state[pageIndex].page,
            taskDelegate = taskDelegate
        )
    }
}