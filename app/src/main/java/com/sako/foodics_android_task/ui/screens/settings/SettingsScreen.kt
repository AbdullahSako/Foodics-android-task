package com.sako.foodics_android_task.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sako.foodics_android_task.R
import com.sako.foodics_android_task.ui.components.MainToolbar

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        MainToolbar(
            modifier = Modifier.padding(top = 10.dp),
            title = stringResource(R.string.settings)
        )
    }
}