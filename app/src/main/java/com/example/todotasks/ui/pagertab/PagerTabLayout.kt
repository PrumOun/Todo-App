package com.example.todotasks.ui.pagertab

import android.util.Log
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.example.todotasks.ID_ADD_NEW_LIST
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
    taskDelegate: TaskDelegate,
) {
    var pageCount by remember { mutableIntStateOf(0) }
    var internalState by remember { mutableStateOf(state) }
    internalState = state
    val pagerState = rememberPagerState { pageCount }
    val scope = rememberCoroutineScope()
    var pendingScrollToNew by remember { mutableStateOf(false) }
    val lastCollectionCount = remember { mutableIntStateOf(state.size) }
    pageCount = state.count {
        it.tab.id != ID_ADD_NEW_LIST
    }

    // Keep current collection id in sync
    LaunchedEffect(Unit) {
        snapshotFlow {
            pagerState.currentPage
        }.collect { currentPage ->
            internalState.getOrNull(currentPage)?.tab?.id?.let { currentCollectionId ->
                taskDelegate.updateCurrentCollectionId(currentCollectionId)
            }
        }
    }

    // When state size changes and a new collection was just added, scroll to it
    LaunchedEffect(state.size) {
        if (pendingScrollToNew && state.isNotEmpty()) {
            val newIndex = state.lastIndex
            pagerState.animateScrollToPage(newIndex)
            pendingScrollToNew = false
        }
        lastCollectionCount.intValue = state.size
    }

    MyTabRowLayout(
        selectedTabIndex = pagerState.currentPage,
        listTabs = state.map { it.tab },
        onTabSelected = { index ->
            val selectedTabId = state.getOrNull(index)?.tab?.id ?: 0
            if (selectedTabId == ID_ADD_NEW_LIST) {
                // Clicked on "＋ New List" tab
                taskDelegate.requestAddNewCollection()
                pendingScrollToNew = true // to scroll once state updates
            }else{
                scope.launch {
                    pagerState.scrollToPage(index)
                    Log.d("PagerTabLayout", "Switched to tab index: $index" )
                }
            }
        },
        onTabLongPressed = {tab ->
            if (tab.id > 0){
                scope.launch {
                    taskDelegate.requestUpdateCollection(tab.id)
                    Log.d("PagerTabLayout", "Long pressed on tab id: ${tab.id}" )
                }
            }
        }
    )
    HorizontalPager(
        state = pagerState,
        key = { it },
        beyondViewportPageCount = 2
    ){ pageIndex ->
        TaskListPage(
            //collectionId = state[pageIndex].tab.id,
            state = state[pageIndex],
            taskDelegate = taskDelegate
        )
    }
}