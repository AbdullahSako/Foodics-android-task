package com.sako.foodics_android_task.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sako.foodics_android_task.data.model.external.Product
import com.sako.foodics_android_task.data.repository.cartRepository.CartRepository
import com.sako.foodics_android_task.data.repository.productRepository.ProductRepository
import com.sako.foodics_android_task.ui.screens.tables.TablesUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam

class MainActivityViewModel constructor(
    @InjectedParam private val cartRepository: CartRepository,
    ): ViewModel() {

    private val _mainUiState = MutableStateFlow<MainUIState>(MainUIState())
    val mainUiState: StateFlow<MainUIState>
        get() = _mainUiState

    init {
        observeCart()
    }

    private fun observeCart(){
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.getCartItemList().collect {
                val totalPrice = it.sumOf { it.price * it.quantity }
                val totalQuantity = it.sumOf { it.quantity }

                _mainUiState.value = _mainUiState.value.copy(cartTotalPrice = totalPrice, cartItemQuantity = totalQuantity)
            }
        }
    }

    fun addToCart(product: Product){
        viewModelScope.launch (Dispatchers.IO){
            cartRepository.addToCart(product)
        }
    }

    fun clearCart(){
        viewModelScope.launch (Dispatchers.IO){
            cartRepository.clearCart()
        }
    }


}

data class MainUIState(
    val cartItemQuantity: Int? = 0,
    val cartTotalPrice : Double? = 0.0
)