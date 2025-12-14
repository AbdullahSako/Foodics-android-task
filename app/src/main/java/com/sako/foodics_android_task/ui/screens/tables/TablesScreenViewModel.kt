package com.sako.foodics_android_task.ui.screens.tables

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TablesScreenViewModel(): ViewModel() {

    private val _tablesUiState = MutableStateFlow<TablesUiState>(TablesUiState())
    val tablesUiState: StateFlow<TablesUiState>
        get() = _tablesUiState

}


data class TablesUiState(val productList : List<Any>? = null)