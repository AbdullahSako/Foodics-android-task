package com.sako.foodics_android_task.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.sako.foodics_android_task.ui.navigation.SettingsScreen

@Composable
fun SettingsScreen(modifier: Modifier = Modifier){
    Box(modifier = Modifier.fillMaxSize()) {
        Text(modifier = Modifier.align(Alignment.Center), text ="Settings Screen")
    }
}