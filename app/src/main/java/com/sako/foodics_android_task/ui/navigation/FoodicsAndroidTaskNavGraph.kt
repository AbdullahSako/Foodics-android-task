package com.sako.foodics_android_task.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay

@Composable
fun FoodicsAndroidTaskNavGraph(modifier: Modifier = Modifier, backStack: NavBackStack<NavKey>) {

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<TablesScreen> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(modifier = Modifier.align(Alignment.Center), text = "Tables Screen")
                }
            }
            entry<OrdersScreen> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(modifier = Modifier.align(Alignment.Center), text ="Orders Screen")
                }
            }
            entry<MenuScreen> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(modifier = Modifier.align(Alignment.Center), text ="Menu Screen")
                }
            }
            entry<SettingsScreen> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(modifier = Modifier.align(Alignment.Center), text ="Settings Screen")
                }
            }
        })


}