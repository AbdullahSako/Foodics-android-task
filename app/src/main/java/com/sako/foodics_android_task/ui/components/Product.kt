package com.sako.foodics_android_task.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sako.foodics_android_task.R

@Composable
fun Product(modifier: Modifier = Modifier) {

    Card(modifier = modifier) {

        Column() {
            Image(
                modifier = Modifier.weight(1f),
                painter = painterResource(R.drawable.img_cheese_burger),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(top = 15.dp, bottom = 30.dp, start = 8.dp, end = 8.dp)
                ) {

                    Text(
                        modifier = Modifier,
                        text = "Cheese Burger",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier,
                        text = "Meat, Cheese, Lettuce, Tomato",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.labelMedium
                    )


                }
                Text(
                    modifier = Modifier.align(Alignment.BottomEnd).padding(end = 8.dp, bottom = 8.dp),
                    text = "3.0 JD",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelSmall
                )
            }


        }

    }


}