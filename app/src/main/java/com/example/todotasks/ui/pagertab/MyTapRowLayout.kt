package com.example.todotasks.ui.pagertab

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.todotasks.ui.pagertab.state.TabUiState

@Composable
fun MyTabRowLayout(
    selectedTabIndex: Int,
    listTabs: List<TabUiState>,
    onTabSelected: (Int) -> Unit = {}
){
    TabRow(selectedTabIndex,
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
}