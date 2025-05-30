    package com.example.sermanna.activity

    import com.example.sermanna.model.SearchViewModel
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.items
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.unit.dp
    import com.example.sermanna.components.SearchBar
    import androidx.lifecycle.viewmodel.compose.viewModel


    @Composable
    fun MessageSearchScreen(viewModel: SearchViewModel = viewModel(), onAccountClick: (Account) -> Unit ) {
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