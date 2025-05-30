package com.example.sermanna.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sermanna.R

@Composable
fun ServicesByCategorySection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Services By Category",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "View all >",
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()) {
            CategoryItem(R.drawable.furniture)
            CategoryItem(R.drawable.appliances)
            CategoryItem(R.drawable.kitchen)
            CategoryItem(R.drawable.technical)
        }
    }
}

@Composable
fun CategoryItem(iconResId: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { /* TODO */ }
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            modifier = Modifier.size(90.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
    }
}
@Preview(showBackground = true)
@Composable
fun prev(){
    ServicesByCategorySection()
}