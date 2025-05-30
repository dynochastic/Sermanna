package com.example.sermanna.activity

import SwipeableBanner
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.sermanna.components.popularServices
import com.example.sermanna.ui.components.ServicesByCategorySection

@Composable
fun HomeScreen(){
    Column{
        SwipeableBanner()
        ServicesByCategorySection()
        popularServices()
    }
}
