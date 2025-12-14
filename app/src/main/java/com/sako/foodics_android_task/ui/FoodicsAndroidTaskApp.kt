package com.sako.foodics_android_task.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.rememberNavBackStack
import com.sako.foodics_android_task.ui.theme.FoodicsandroidtaskTheme

@Composable
fun FoodicsAndroidTaskApp() {
    FoodicsandroidtaskTheme() {
        val backStack = rememberNavBackStack(TablesScreen)

        Scaffold() { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                FoodicsAndroidTaskNavGraph(
                    modifier = Modifier.padding(innerPadding),
                    backStack = backStack
                )
            }
        }


    }
}