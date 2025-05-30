package com.example.sermanna.activity

import PrivateChatViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.sermanna.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivateChatScreen(
    senderEmail: String,
    receiverEmail: String,
    onBack: () -> Unit = {},
    onViewProfile: (String) -> Unit = {}
) {
    val viewModel: PrivateChatViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()
    val context = LocalContext.current

    var receiverAccount by remember(receiverEmail) { mutableStateOf<Account?>(null) }

    LaunchedEffect(receiverEmail) {
        viewModel.loadReceiverInfo(receiverEmail) { account ->
            receiverAccount = account
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            receiverAccount?.Email?.let(onViewProfile)
                        }
                    ) {
                        receiverAccount?.let {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = it.profileImageUrl,
                                    error = painterResource(R.drawable.user1),
                                    placeholder = painterResource(R.drawable.user1)
                                ),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${it.Firstname} ${it.Lastname}")
                        } ?: Text("Loading...")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top
                ) {
                    items(messages) { message ->
                        Text(
                            text = "${message.}: ${message.}",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = viewModel.messageText,
                    onValueChange = { viewModel.messageText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    placeholder = { Text("Type a message") },
                    maxLines = 3,
                    trailingIcon = {
                        IconButton(onClick = { viewModel.sendMessage() }) {
                            Icon(painterResource(id = R.drawable.ic_send), contentDescription = "Send")
                        }
                    }
                )
            }
        }
    )
}
