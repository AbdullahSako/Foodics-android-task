package com.sako.foodics_android_task.ui.screens.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sako.foodics_android_task.R
import com.sako.foodics_android_task.ui.components.MainToolbar

@Composable
fun MenuScreen(modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxSize()) {
        MainToolbar(
            modifier = Modifier.padding(top = 10.dp),
            title = stringResource(R.string.menu)
        )
    }
}