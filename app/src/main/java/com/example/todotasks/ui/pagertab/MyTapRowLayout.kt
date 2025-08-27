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
    onTabSelected: (Int) -> Unit = {}
){
    if(listTabs.size <= 3){
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    width = Dp.Unspecified
                )
            }) {
            repeat(listTabs.size) { tabIndex ->
                TabItemLayout(
                    state = listTabs[tabIndex],
                    isSelected = selectedTabIndex == tabIndex,
                    onTabSelected = { onTabSelected(tabIndex) }
                )
            }
        }
    }else{
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                TabRowDefaults.PrimaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    width = Dp.Unspecified
                )
            }) {
            repeat(listTabs.size) { tabIndex ->
                TabItemLayout(
                    state = listTabs[tabIndex],
                    isSelected = selectedTabIndex == tabIndex,
                    onTabSelected = { onTabSelected(tabIndex) }
                )
            }
        }
    }

}