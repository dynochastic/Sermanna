package com.example.sermanna.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent() {
    Box(
        Modifier
            .fillMaxHeight()
            .width(LocalConfiguration.current.screenWidthDp.dp / 2)
            .background(Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Menu",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Spacer(Modifier.height(16.dp))
            Text("Home", color = Color.Black)
            Text("Profile", color = Color.Black)
            Text("Settings", color = Color.Black)
        }
    }
}