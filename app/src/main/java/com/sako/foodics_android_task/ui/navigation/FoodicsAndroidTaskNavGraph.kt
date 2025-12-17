package com.sako.foodics_android_task.ui.navigation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.sako.foodics_android_task.data.model.external.Product
import com.sako.foodics_android_task.ui.screens.menu.MenuScreen
import com.sako.foodics_android_task.ui.screens.orders.OrdersScreen
import com.sako.foodics_android_task.ui.screens.settings.SettingsScreen
import com.sako.foodics_android_task.ui.screens.tables.TablesScreen

@Composable
fun FoodicsAndroidTaskNavGraph(modifier: Modifier = Modifier, navBackStack: NavBackStack<NavKey>,onProductClick: (product: Product) -> Unit ) {

    NavDisplay(
        modifier = modifier,
        backStack = navBackStack,
        onBack = { navBackStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<TablesScreenKey> {
                TablesScreen(Modifier.statusBarsPadding().navigationBarsPadding().padding(horizontal = 15.dp), onProductClick = {onProductClick.invoke(it)})
            }
            entry<OrdersScreenKey> {
                OrdersScreen(Modifier.statusBarsPadding().padding(horizontal = 15.dp))
            }
            entry<MenuScreenKey> {
                MenuScreen(Modifier.statusBarsPadding().padding(horizontal = 15.dp))
            }
            entry<SettingsScreenKey> {
                SettingsScreen(Modifier.statusBarsPadding().padding(horizontal = 15.dp))
            }
        })


}