package com.sako.foodics_android_task.ui.screens.tables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.external.Product
import com.sako.foodics_android_task.data.repository.categoryRepository.CategoryRepository
import com.sako.foodics_android_task.data.repository.productRepository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.sako.foodics_android_task.utils.resultWrapper.RefreshResult
import org.koin.core.annotation.InjectedParam

class TablesScreenViewModel(
    @InjectedParam private val productRepository: ProductRepository,
    @InjectedParam private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _tablesUiState = MutableStateFlow<TablesUiState>(TablesUiState())
    val tablesUiState: StateFlow<TablesUiState>
        get() = _tablesUiState

    init {
        refreshProducts()
        refreshCategories()

        observeProductList()
        observeCategoryList()
    }


    /**
     * Refresh category list from data source
     * */
    private fun refreshCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.refreshCategories().collect {
                _tablesUiState.value = _tablesUiState.value.copy(categoryListRefreshResult = it)
            }
        }
    }


    /**
     * Observe changes on category list
     * */
    private fun observeCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.loadCategories().collect {
                _tablesUiState.value = _tablesUiState.value.copy(categoryList = it)
            }
        }
    }

    /**
     * Refresh product list from data source
     * */
    private fun refreshProducts(){
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.refreshProductList().collect {
                _tablesUiState.value = _tablesUiState.value.copy(productListRefreshResult = it)
            }
        }
    }


    /**
     * Observe changes on product list
     * */
    private fun observeProductList(){
        viewModelScope.launch (Dispatchers.IO){
            productRepository.loadProductList().collect {
                _tablesUiState.value = _tablesUiState.value.copy(productList = it)
            }
        }
    }


}


data class TablesUiState(
    val productList: List<Product>? = null,
    val categoryList: List<Category>? = null,
    val productListRefreshResult: RefreshResult? = null,
    val categoryListRefreshResult: RefreshResult? = null

)