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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sako.foodics_android_task.R
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.external.Product
import com.sako.foodics_android_task.ui.components.MainToolbar
import com.sako.foodics_android_task.ui.components.Product
import com.sako.foodics_android_task.utils.ext.loge
import com.sako.foodics_android_task.utils.resultWrapper.RefreshResult
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun TablesScreen(
    modifier: Modifier = Modifier,
    viewModel: TablesScreenViewModel = koinViewModel(),
    onProductClick: (product: Product) -> Unit
) {
    val searchKey = remember { mutableStateOf("") }
    val selectedTabIndex = remember { mutableIntStateOf(0) }


    val uiState by viewModel.tablesUiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val localResource = LocalResources.current


    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier) {
            MainToolbar(
                modifier = Modifier.padding(top = 10.dp),
                title = stringResource(R.string.tables)
            )
            ProductsSearchBar(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp), searchKey = searchKey,
                onSearch = { query ->
                    viewModel.setFilters(
                        query,
                        uiState.categoryList?.getOrNull(selectedTabIndex.intValue)
                    )
                })
            ProductsCategoryTabRow(
                Modifier,
                uiState,
                selectedTabIndex,
                onTabSelected = { category ->
                    viewModel.setFilters(searchKey.value, category)
                })
            ProductsGridList(Modifier, uiState = uiState, onProductClick = {onProductClick.invoke(it)})


        }

        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackBarHostState
        )
    }


    //shows a snack bar on data source refresh error
    LaunchedEffect(uiState.productListRefreshResult) {
        if (uiState.productListRefreshResult is RefreshResult.Error) {
            uiState.productListRefreshResult?.errorType.loge("NetworkError", "ProductsAPI")
            snackBarHostState.showSnackbar(localResource.getString(R.string.something_went_wrong))
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsSearchBar(
    modifier: Modifier = Modifier,
    searchKey: MutableState<String>,
    onSearch: (String) -> Unit
) {

    SearchBar(
        modifier = modifier, inputField =
            {
                SearchBarDefaults.InputField(
                    query = searchKey.value,
                    onQueryChange = { searchKey.value = it },
                    onSearch = {
                        onSearch.invoke(searchKey.value)
                    },
                    expanded = false,
                    onExpandedChange = { },
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
                                    onSearch.invoke(searchKey.value)
                                }),
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.cd_clear_search)
                            )
                        }
                    } else null
                )
            }, expanded = false, onExpandedChange = {}, content = {})

    //invoke search on each letter change
    LaunchedEffect(searchKey.value) {
        onSearch.invoke(searchKey.value)
    }

}

@Composable
fun ProductsCategoryTabRow(
    modifier: Modifier = Modifier,
    uiState: TablesUiState,
    selectedTabIndex: MutableIntState,
    onTabSelected: (Category?) -> Unit
) {

    AnimatedContent(uiState.categoryList) {
        if (!it.isNullOrEmpty()) {
            PrimaryScrollableTabRow(
                modifier = modifier,
                selectedTabIndex = selectedTabIndex.intValue
            ) {
                it.forEachIndexed { index, category ->
                    Tab(
                        text = { Text(category.name ?: "") },
                        selected = selectedTabIndex.intValue == index,
                        onClick = {
                            selectedTabIndex.intValue = index
                        })
                }
            }
        }

        //invoke on tab selected everytime selected tab index changes
        LaunchedEffect(selectedTabIndex.intValue) {
            onTabSelected(uiState.categoryList?.getOrNull(selectedTabIndex.intValue))
        }

    }

}

@Composable
fun ProductsGridList(
    modifier: Modifier = Modifier,
    uiState: TablesUiState,
    onProductClick: (product: Product) -> Unit
) {


    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 148.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(top = 15.dp, bottom = 75.dp)
    ) {
        items(items= uiState.productList ?: listOf(), key = {it.id?: UUID.randomUUID()}) { productItem ->
            Product(
                modifier = Modifier.heightIn(max = 256.dp).animateItem().clickable(onClick = {
                    onProductClick.invoke(productItem)
                }),
                title = productItem.name ?: "",
                subtitle = productItem.description ?: "",
                price = productItem.price.toString()
            )
        }
    }


}