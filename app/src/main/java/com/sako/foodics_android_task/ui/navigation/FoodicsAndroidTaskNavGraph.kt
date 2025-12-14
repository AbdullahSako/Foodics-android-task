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
import com.sako.foodics_android_task.ui.screens.menu.MenuScreen
import com.sako.foodics_android_task.ui.screens.orders.OrdersScreen
import com.sako.foodics_android_task.ui.screens.settings.SettingsScreen
import com.sako.foodics_android_task.ui.screens.tables.TablesScreen

@Composable
fun FoodicsAndroidTaskNavGraph(modifier: Modifier = Modifier, backStack: NavBackStack<NavKey>) {

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<TablesScreen> {
                TablesScreen()
            }
            entry<OrdersScreen> {
                OrdersScreen()
            }
            entry<MenuScreen> {
                MenuScreen()
            }
            entry<SettingsScreen> {
                SettingsScreen()
            }
        })


}