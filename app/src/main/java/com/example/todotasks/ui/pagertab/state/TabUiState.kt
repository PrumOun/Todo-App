package com.example.todotasks.ui.pagertab.state

import com.example.todotasks.database.entity.SortType
import com.example.todotasks.database.entity.TaskCollection
import com.example.todotasks.database.entity.toSortType

data class TabUiState(
    val id: Long,
    val title: String,
    val sortType: SortType
)

fun TaskCollection.toTabUiState(): TabUiState {
    return TabUiState(
        id = this.id,
        title = this.title,
        sortType = this.sortType.toSortType()
    )
}