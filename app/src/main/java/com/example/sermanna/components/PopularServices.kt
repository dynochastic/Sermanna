package com.example.sermanna.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sermanna.R

@Composable
fun popularServices(){
    Column (modifier = Modifier.background(Color( 0xffffdf00))) {
        Row(Modifier.padding(top = 5.dp, start = 5.dp)){
            Text( "Popular Services", style = MaterialTheme.typography.titleMedium)
        }
        Row(Modifier.fillMaxWidth().background(Color(0xffffdf00)).padding(5.dp), horizontalArrangement = Arrangement.SpaceBetween){
            popularCategories(R.drawable.cleaning_service)
            popularCategories(R.drawable.handyman)
            popularCategories(R.drawable.furniture_assembly)
        }
    }
}

@Composable
fun popularCategories(iconResId: Int, onClick: () -> Unit = {}) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
                .clickable(onClick = onClick)
        )
}

@Preview(showBackground = true)
@Composable
fun prevs(){
    popularServices()
}