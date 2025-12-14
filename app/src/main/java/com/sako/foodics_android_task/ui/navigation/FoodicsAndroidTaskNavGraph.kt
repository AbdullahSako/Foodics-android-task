package com.sako.foodics_android_task.ui.navigation

import androidx.compose.runtime.Composable
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
fun FoodicsAndroidTaskNavGraph(modifier: Modifier = Modifier, navBackStack: NavBackStack<NavKey>) {

    NavDisplay(
        modifier = modifier,
        backStack = navBackStack,
        onBack = { navBackStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<TablesScreenKey> {
                TablesScreen()
            }
            entry<OrdersScreenKey> {
                OrdersScreen()
            }
            entry<MenuScreenKey> {
                MenuScreen()
            }
            entry<SettingsScreenKey> {
                SettingsScreen()
            }
        })


}