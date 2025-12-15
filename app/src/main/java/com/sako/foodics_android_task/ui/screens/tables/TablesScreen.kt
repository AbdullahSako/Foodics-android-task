package com.sako.foodics_android_task.ui.screens.tables

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sako.foodics_android_task.R
import com.sako.foodics_android_task.ui.components.MainToolbar
import com.sako.foodics_android_task.ui.components.Product
import com.sako.foodics_android_task.utils.ext.loge
import com.sako.foodics_android_task.utils.resultWrapper.Result
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TablesScreen(
    modifier: Modifier = Modifier,
    viewModel: TablesScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.tablesUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()


    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier) {
            MainToolbar(
                modifier = Modifier.padding(top = 10.dp),
                title = stringResource(R.string.tables)
            )
            ProductsSearchBar(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
            ProductsCategoryTabRow(Modifier, uiState)
            ProductsGridList(Modifier, uiState = uiState, onShowSnackBar = { message ->
                coroutineScope.launch {
                    snackBarHostState.showSnackbar("Something went wrong.")
                }
            })


        }

        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackBarHostState
        )
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
fun ProductsCategoryTabRow(modifier: Modifier = Modifier, uiState: TablesUiState) {
    val selectedIndex = remember { mutableStateOf(0) }

    AnimatedContent(uiState.categoryListResult?.data) {
        if (!it.isNullOrEmpty()) {
            PrimaryScrollableTabRow(modifier = modifier, selectedTabIndex = selectedIndex.value) {
                it.forEachIndexed { index, category ->
                    Tab(
                        text = { Text(category.name ?: "") },
                        selected = selectedIndex.value == index,
                        onClick = { selectedIndex.value = index })
                }
            }
        }
    }

}

@Composable
fun ProductsGridList(
    modifier: Modifier = Modifier,
    uiState: TablesUiState,
    onShowSnackBar: (message: String) -> Unit
) {


    when (uiState.productListResult) {
        is Result.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        is Result.Success -> {
            LazyVerticalGrid(
                modifier = modifier,
                columns = GridCells.Adaptive(minSize = 148.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(vertical = 15.dp)
            ) {
                items(uiState.productListResult.data ?: listOf()) { productItem ->
                    Product(
                        modifier = Modifier.heightIn(max = 256.dp),
                        title = productItem.name ?: "",
                        subtitle = productItem.description ?: "",
                        price = productItem.price.toString()
                    )
                }
            }
        }

        is Result.Error -> {
            uiState.productListResult.errorType.loge("NetworkError","ProductsAPI")
            onShowSnackBar.invoke("Something went wrong.")
        }

        else -> {}
    }


}