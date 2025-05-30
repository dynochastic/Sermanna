@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.sermanna.activity

import com.example.sermanna.model.SearchViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sermanna.components.SearchBar

@Composable
fun SearchAccountsScreen(
    viewModel: SearchViewModel = viewModel(),
    onAccountClick: (Account) -> Unit //
) {
    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        SearchBar(
            query = query,
            onQueryChange = {
                  query = it
                viewModel.search(it)
            },
            modifier = Modifier.padding(8.dp)
        )

        if (viewModel.searchResults.isEmpty() && query.isNotBlank()) {
            Text("No results found", modifier = Modifier.padding(16.dp), color = Color.Gray)
        } else {
            LazyColumn {
                items(viewModel.searchResults) { account ->
                    AccountListItem(account = account, onClick = { onAccountClick(account) })
                }
            }
        }
    }
}

@Composable
fun AccountListItem(account: Account, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
            .background(Color(0xFFECECEC), shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        AsyncImage(
            model = account.profileImageUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = "${account.Firstname} ${account.Lastname}")
            Text(text = account.Email, color = Color.Gray)
        }
    }
}

data class Account(
    val Email: String = "",
    val Firstname: String = "",
    val Lastname: String = "",
    val profileImageUrl: String? = null
)
