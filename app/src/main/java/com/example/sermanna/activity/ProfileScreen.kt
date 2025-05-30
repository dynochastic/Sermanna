package com.example.sermanna.activity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sermanna.R
import com.google.firebase.firestore.FirebaseFirestore
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun ProfileScreen(firstName: String, lastName: String, userEmail: String, password: String, navController: NavController? = null, onLogOut: () -> Unit
) {
    val fullName = "$firstName $lastName"
    val db = FirebaseFirestore.getInstance()

    var bio by remember { mutableStateOf("") }
    val profileImageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }

// In the LaunchedEffect block, It decode Base64 string
    LaunchedEffect(userEmail) {
        db.collection("Accounts")
            .whereEqualTo("Email", userEmail)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val document = result.documents[0]
                    bio = document.getString("Bio") ?: "Enter Bio"
                    val base64String = document.getString("ProfileImage")
                    if (!base64String.isNullOrBlank()) {
                        try {
                            val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            profileImageBitmap.value = bitmap?.asImageBitmap()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    bio = "Bio not found"
                }
            }
            .addOnFailureListener {
                bio = "Failed to load bio"
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { navController?.navigate("editAccount/$firstName/$lastName/$userEmail/$password") },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (profileImageBitmap.value != null) {
                Image(
                    bitmap = profileImageBitmap.value!!,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(2.dp, Color.Black), CircleShape)
                )
            } else {
                // fallback to local image if not loaded
                Image(
                    painter = painterResource(id = R.drawable.user1),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(2.dp, Color.Black), CircleShape)
                )
            }

            Text(
                text = fullName,
                fontSize = 24.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = bio,
                fontSize = 16.sp,
            )
        }

        Button(
            onClick = { onLogOut() },
            modifier = Modifier
                .width(160.dp)
                .padding(top = 16.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Log Out")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(firstName = "Taper", lastName = "Fade", userEmail = "lowtaperfade@gmail.com", password = "imagine", onLogOut = {}
    )
}