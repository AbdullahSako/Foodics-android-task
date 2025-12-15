package com.sako.foodics_android_task.ui.screens.tables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sako.foodics_android_task.data.model.external.Category
import com.sako.foodics_android_task.data.model.external.Product
import com.sako.foodics_android_task.data.repository.categoryRepository.CategoryRepository
import com.sako.foodics_android_task.data.repository.productRepository.ProductRepository
import com.sako.foodics_android_task.utils.ext.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.sako.foodics_android_task.utils.resultWrapper.Result

class TablesScreenViewModel(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _tablesUiState = MutableStateFlow<TablesUiState>(TablesUiState())
    val tablesUiState: StateFlow<TablesUiState>
        get() = _tablesUiState

    init {
        loadAndObserveCategoryList()
        loadAndObserveProductList()
    }


    private fun loadAndObserveCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.loadCategoriesList().collect {
                _tablesUiState.value = _tablesUiState.value.copy(categoryListResult = it)
            }
        }
    }

    private fun loadAndObserveProductList(){
        viewModelScope.launch (Dispatchers.IO){
            productRepository.loadProductList().collect {
                _tablesUiState.value = _tablesUiState.value.copy(productListResult = it)
            }
        }
    }


}


data class TablesUiState(
    val productListResult: Result<List<Product>>? = null,
    val categoryListResult: Result<List<Category>>? = null
)