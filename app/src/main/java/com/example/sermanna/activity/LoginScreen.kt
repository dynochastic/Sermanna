package com.example.sermanna.activity

import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import com.example.sermanna.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import com.google.firebase.firestore.BuildConfig
import com.google.firebase.firestore.FirebaseFirestore


fun CustomToast(context: android.content.Context, message: String) {
    val toast = Toast(context)
    val textView = TextView(context).apply {
        text = message
        setTextColor(android.graphics.Color.WHITE)
        textSize = 16f
        setPadding(32, 16, 32, 16)
        background = GradientDrawable().apply {
            cornerRadius = 42f
            setColor(android.graphics.Color.GRAY)
        }
    }
    toast.view = textView
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
}

@Composable
fun LoginScreen(navController: NavController, onNavigateToHomeScreen: (String, String, String, String) -> Unit){

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val topPadding = (screenHeight * .23f).dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding),
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.loginlogo),
                contentDescription = "App Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(440.dp)
                    .height(180.dp)
            )
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp)
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                placeholder = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp)
            )

            Button(
                onClick = {
                    val enteredEmail = email.value.trim()
                    val enteredPassword = password.value.trim()

                    if (enteredEmail.isNotEmpty() && enteredPassword.isNotEmpty()) {
                        db.collection("Accounts")
                            .whereEqualTo("Email", enteredEmail)
                            .whereEqualTo("Password", enteredPassword)
                            .get()
                            .addOnSuccessListener { result ->
                                if (!result.isEmpty) {
                                    val doc = result.documents[0]
                                    val firstName = doc.getString("Firstname") ?: ""
                                    val lastName = doc.getString("Lastname") ?: ""
                                    onNavigateToHomeScreen(
                                        firstName,
                                        lastName,
                                        enteredEmail,
                                        enteredPassword
                                    )
                                } else {
                                    CustomToast(context, "User does not exist.")
                                }
                            }
                            .addOnFailureListener { exception ->
                                CustomToast(context, "Error: ${exception.message}")
                            }
                    } else {
                        CustomToast(context, "Please enter email and password.")
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(120.dp)
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White
                )
            ) {
                Text("Log In", fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "--- or ---",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                TextButton(
                    onClick = { navController.navigate("signup") },
                    contentPadding = PaddingValues(start = 0.dp)
                ) {
                    Text(
                        text = "Sign up",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 22.sp
                    )
                }
            }
        }

        Text(
            text = "All Rights Reserved",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController(), onNavigateToHomeScreen = {firstName: String, lastName: String, userEmail: String, password: String -> })
}