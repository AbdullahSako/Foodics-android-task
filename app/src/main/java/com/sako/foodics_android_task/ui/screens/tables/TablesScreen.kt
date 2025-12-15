package com.sako.foodics_android_task.ui.screens.tables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sako.foodics_android_task.R
import com.sako.foodics_android_task.ui.components.MainToolbar
import com.sako.foodics_android_task.ui.components.Product
import org.koin.androidx.compose.koinViewModel

@Composable
fun TablesScreen(
    modifier: Modifier = Modifier,
    viewModel: TablesScreenViewModel = koinViewModel()
) {

    Column(modifier = modifier.fillMaxSize()) {
        MainToolbar(modifier = Modifier.padding(top = 10.dp), title = stringResource(R.string.tables))
        ProductsSearchBar(Modifier
            .fillMaxWidth()
            .padding(top = 10.dp))
        ProductsCategoryTabRow(Modifier)
        ProductsGridList(Modifier)

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsSearchBar(modifier: Modifier = Modifier) {
    val searchKey = remember { mutableStateOf("") }
    val isExpanded = remember { mutableStateOf(false) }



    SearchBar(
        modifier = modifier, inputField =
            {
                SearchBarDefaults.InputField(
                    query = searchKey.value,
                    onQueryChange = { searchKey.value = it },
                    onSearch = {},
                    expanded = isExpanded.value,
                    onExpandedChange = { isExpanded.value = it },
                    placeholder = { Text(stringResource(R.string.search_hint)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = stringResource(R.string.search_hint)
                        )
                    },
                    trailingIcon = if (searchKey.value.isNotEmpty()) {
                        {
                            Icon(
                                modifier = Modifier.clickable(onClick = {
                                    searchKey.value = ""
                                }),
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.cd_clear_search)
                            )
                        }
                    } else null
                )
            }, expanded = false, onExpandedChange = {}, content = {})
}

@Composable
fun ProductsCategoryTabRow(modifier: Modifier = Modifier) {
    val selectedIndex = remember { mutableStateOf(0) }

    PrimaryTabRow(modifier = modifier, selectedTabIndex = selectedIndex.value) {
        listOf("A", "B", "C", "D", "E", "F", "F1", "F2", "F3").forEach {
            Tab(text = { Text(it) }, selected = false, onClick = {})
        }

    }
}

@Composable
fun ProductsGridList(modifier: Modifier = Modifier) {
    LazyVerticalGrid(modifier= modifier,
        columns = GridCells.Adaptive(minSize = 148.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(vertical = 15.dp)
    ) {
        items(25) {
            Product(modifier = Modifier.heightIn(max = 256.dp))
        }
    }
}