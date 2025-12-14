package com.sako.foodics_android_task.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.rememberNavBackStack
import com.sako.foodics_android_task.ui.navigation.FoodicsAndroidTaskNavGraph
import com.sako.foodics_android_task.ui.navigation.NavSuiteItem
import com.sako.foodics_android_task.ui.navigation.TablesScreenKey
import com.sako.foodics_android_task.ui.theme.FoodicsandroidtaskTheme

@Composable
fun FoodicsAndroidTaskApp() {
    FoodicsandroidtaskTheme() {
        val navBackStack = rememberNavBackStack(TablesScreenKey)
        var currentSelectedNavSuiteItem by rememberSaveable { mutableStateOf(NavSuiteItem.TABLES) }


        NavigationSuiteScaffold(navigationSuiteItems = {
            NavSuiteItem.entries.forEach {
                item(
                    selected = it == currentSelectedNavSuiteItem,
                    icon = { Icon(it.icon, contentDescription = null) },
                    label = { Text(text = stringResource(it.label)) },
                    onClick = {
                        currentSelectedNavSuiteItem = it
                        navBackStack.removeLastOrNull()
                        navBackStack.add(it.navKey)
                    })
            }

        }) {
            Box(modifier = Modifier.fillMaxSize()) {
                FoodicsAndroidTaskNavGraph(
                    navBackStack = navBackStack
                )
            }
        }


    }
}