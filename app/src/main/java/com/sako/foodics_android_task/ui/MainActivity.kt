package com.sako.foodics_android_task.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainUIState by viewModel.mainUiState.collectAsStateWithLifecycle()

            FoodicsAndroidTaskApp(
                mainUIState = mainUIState,
                onViewOrderClick = {
                    viewModel.clearCart()
                },
                onProductClick = {
                    viewModel.addToCart(it)
                })
        }
    }
}
