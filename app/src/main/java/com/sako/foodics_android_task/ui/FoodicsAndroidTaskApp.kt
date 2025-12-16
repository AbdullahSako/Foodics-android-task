package com.sako.foodics_android_task.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.rememberNavBackStack
import com.sako.foodics_android_task.R
import com.sako.foodics_android_task.data.model.external.Product
import com.sako.foodics_android_task.ui.navigation.FoodicsAndroidTaskNavGraph
import com.sako.foodics_android_task.ui.navigation.NavSuiteItem
import com.sako.foodics_android_task.ui.navigation.TablesScreenKey
import com.sako.foodics_android_task.ui.theme.FoodicsandroidtaskTheme
import com.sako.foodics_android_task.utils.ext.logd

@Composable
fun FoodicsAndroidTaskApp(
    mainUIState: MainUIState,
    onViewOrderClick: () -> Unit,
    onProductClick: (product: Product) -> Unit
) {
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
                    navBackStack = navBackStack,
                    onProductClick = { onProductClick.invoke(it) }
                )

                ViewOrderButton(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    mainUIState = mainUIState,
                    onViewOrderClick = {
                        onViewOrderClick.invoke()
                    })

            }
        }


    }

}

@Composable
fun ViewOrderButton(
    modifier: Modifier = Modifier,
    mainUIState: MainUIState,
    onViewOrderClick: () -> Unit
) {
    mainUIState.cartItemQuantity.logd()

    AnimatedVisibility(
        modifier = modifier,
        visible = (mainUIState.cartItemQuantity != 0) && mainUIState.cartTotalPrice != 0.0,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {

        Box(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, bottom = 4.dp)
                .clip(
                    RoundedCornerShape(4.dp)
                )
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .clickable(onClick = {
                    onViewOrderClick.invoke()
                })
                .padding(vertical = 10.dp, horizontal = 15.dp)

        ) {

            Row(modifier = Modifier.align(Alignment.CenterStart)) {

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary)
                        .size(32.dp)
                ) {
                    AnimatedContent(modifier = Modifier.align(Alignment.Center), targetState = mainUIState.cartItemQuantity) { quantity ->
                        Text(
                            text = quantity.toString(),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                }

                Text(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically),
                    text = stringResource(R.string.view_order),
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }


            Row(modifier = Modifier.align(Alignment.CenterEnd)) {

                AnimatedContent(mainUIState.cartTotalPrice) { price ->
                    Text(
                        text = "${String.format("%.2f", price)} JD",
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }


                Icon(
                    modifier = Modifier.padding(start = 8.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }


        }
    }
}