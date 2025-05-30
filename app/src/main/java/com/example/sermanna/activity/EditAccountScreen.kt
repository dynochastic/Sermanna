package com.example.sermanna.activity

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.sermanna.R
import com.google.firebase.firestore.FirebaseFirestore
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.util.Base64
import java.io.ByteArrayOutputStream

@Composable
fun EditAccount(firstNameInitial: String = "", lastNameInitial: String = "", userEmail: String, password: String, onBack: (() -> Unit)? = null, onSave: ((String, String) -> Unit)? = null
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val base64Image = remember { mutableStateOf("") }

    val firstName = remember { mutableStateOf(firstNameInitial) }
    val lastName = remember { mutableStateOf(lastNameInitial) }
    val bio = remember { mutableStateOf("") }
    val profileImageBitmap = remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }

    // image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri.value = it
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes = baos.toByteArray()
            base64Image.value = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            profileImageBitmap.value = bitmap.asImageBitmap()
        }
    }

    // Fetch the bio and profile image Base64 from Firestore
    LaunchedEffect(userEmail) {
        db.collection("Accounts")
            .whereEqualTo("Email", userEmail)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    bio.value = document.getString("Bio") ?: ""
                    // loads Base64 image string
                    val imageString = document.getString("ProfileImage")
                    if (!imageString.isNullOrEmpty()) {
                        try {
                            val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
                            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            profileImageBitmap.value = bitmap?.asImageBitmap()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            profileImageBitmap.value = null
                        }
                    }
                } else {
                    bio.value = "Enter Bio"
                    profileImageBitmap.value = null
                }
            }
            .addOnFailureListener {
                bio.value = "Enter Bio"
                profileImageBitmap.value = null
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .align(Alignment.TopStart)
                .clickable { onBack?.invoke() }
                .padding(8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // will show selected or fetched profile image, or default
            when {
                imageUri.value != null -> {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUri.value),
                        contentDescription = "Selected Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.Black), CircleShape)
                    )
                }
                profileImageBitmap.value != null -> {
                    Image(
                        bitmap = profileImageBitmap.value!!,
                        contentDescription = "Profile Picture from Firestore",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.Black), CircleShape)
                    )
                }
                else -> {
                    Image(
                        painter = painterResource(id = R.drawable.user1),
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(BorderStroke(2.dp, Color.Black), CircleShape)
                    )
                }
            }

            TextButton(
                onClick = { launcher.launch("image/*") },
                contentPadding = PaddingValues(start = 0.dp)
            ) {
                Text(
                    text = "Change profile picture",
                    color = Color.Blue,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = firstName.value,
                onValueChange = { firstName.value = it },
                singleLine = true,
                label = { Text("Firstname") },
                textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = lastName.value,
                onValueChange = { lastName.value = it },
                singleLine = true,
                textStyle = TextStyle(fontSize = 18.sp, color = Color.Black),
                label = { Text("Lastname") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = bio.value,
                onValueChange = { bio.value = it },
                label = { Text("Enter Bio") },
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (firstName.value.isBlank() || lastName.value.isBlank() || bio.value.isBlank()) {
                        CustomToast(context, "Please fill all fields")
                        return@Button
                    }

                    val accountsRef = db.collection("Accounts")
                    accountsRef.whereEqualTo("Email", userEmail).get()
                        .addOnSuccessListener { querySnapshot ->
                            if (!querySnapshot.isEmpty) {
                                val document = querySnapshot.documents[0]
                                val userRef = document.reference

                                userRef.update(
                                    mapOf(
                                        "Firstname" to firstName.value,
                                        "Lastname" to lastName.value,
                                        "Bio" to bio.value,
                                        "ProfileImage" to base64Image.value
                                    )
                                ).addOnSuccessListener {
                                    CustomToast(context, "Profile updated")
                                    onSave?.invoke(firstName.value, lastName.value)
                                }.addOnFailureListener {
                                    CustomToast(context, "Error updating profile")
                                }
                            } else {
                                accountsRef.add(
                                    mapOf(
                                        "Email" to userEmail,
                                        "Firstname" to firstName.value,
                                        "Lastname" to lastName.value,
                                        "Password" to password,
                                        "Bio" to bio.value,
                                        "ProfileImage" to base64Image.value
                                    )
                                ).addOnSuccessListener {
                                    CustomToast(context, "Profile created")
                                    onSave?.invoke(firstName.value, lastName.value)
                                }.addOnFailureListener {
                                    CustomToast(context, "Error saving profile")
                                }
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White
                )
            ) {
                Text("Edit Profile")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditAccountPreview() {
    EditAccount(firstNameInitial = "Low", lastNameInitial = "Taper", userEmail = "lowtaperfade@gmail.com", password = "imagine"
    )
}