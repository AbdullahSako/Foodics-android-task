package com.sako.foodics_android_task.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.sako.foodics_android_task.R

enum class NavSuiteItem(
    @StringRes val label: Int,
    val icon: ImageVector,
    val navKey: NavKey
) {

    TABLES(R.string.tables, Icons.Default.Restaurant, TablesScreenKey),
    ORDERS(R.string.orders, Icons.AutoMirrored.Filled.MenuBook, OrdersScreenKey),
    MENU(R.string.menu, Icons.Default.LocalDining, MenuScreenKey),
    SETTINGS(R.string.settings, Icons.Default.Settings, SettingsScreenKey);

}