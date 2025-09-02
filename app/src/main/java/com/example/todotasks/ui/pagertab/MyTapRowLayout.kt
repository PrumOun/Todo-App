package com.example.todotasks.ui.pagertab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.todotasks.ui.pagertab.state.TabUiState

@Composable
fun MyTabRowLayout(
    selectedTabIndex: Int,
    listTabs: List<TabUiState>,
    onTabSelected: (Int) -> Unit = {},
    onTabLongPressed: (TabUiState) -> Unit = {}
){
    val isScrollable = listTabs.size > 3
    val rowModifier = Modifier.fillMaxWidth()

    val rowContent: @Composable () -> Unit = {
        repeat(listTabs.size) { index ->
            TabItemLayout(
                state = listTabs[index],
                isSelected = selectedTabIndex == index,
                onTabSelected = { onTabSelected(index) },
                onTabLongPressed = { onTabLongPressed(listTabs[index]) }
            )
        }
    }

    if (isScrollable) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = rowModifier,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            },
            tabs = rowContent
        )
    } else {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = rowModifier,
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            },
            tabs = rowContent
        )
    }
}