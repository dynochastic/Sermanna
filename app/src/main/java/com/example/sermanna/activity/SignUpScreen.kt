package com.example.sermanna.activity

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.sermanna.R
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun SignUpScreen(navController: NavController){

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val email = remember { mutableStateOf("") }
    val fn = remember { mutableStateOf("") }
    val ln = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val topPadding = (screenHeight * .13f).dp

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
        ){
            Image(
                painter = painterResource(id = R.drawable.signuplogo),
                contentDescription = "App Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(240.dp)
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
                value = fn.value,
                onValueChange = { fn.value = it },
                placeholder = { Text("Firstname") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp)
            )

            OutlinedTextField(
                value = ln.value,
                onValueChange = { ln.value = it },
                placeholder = { Text("Lastname") },
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
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                textStyle = TextStyle(fontSize = 18.sp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp)
            )

            Button(  onClick = {
                val e = email.value.trim()
                val f = fn.value.trim()
                val l = ln.value.trim()
                val p = password.value.trim()


                if (e.isEmpty() || f.isEmpty() || l.isEmpty() || p.isEmpty()) {
                    CustomToast(context, "Please fill in all fields")
                } else {
                    db.collection("Accounts")
                        .whereEqualTo("Email", e)
                        .get()
                        .addOnSuccessListener { result ->
                            if (!result.isEmpty) {
                                CustomToast(context, "Account already exists")
                            } else {
                                val newUser = hashMapOf(
                                    "Email" to e,
                                    "Firstname" to f,
                                    "Lastname" to l,
                                    "Password" to p,
                                    "fullname" to "${f.trim()} ${l.trim()}".lowercase()

                                )

                                db.collection("Accounts")
                                    .add(newUser)
                                    .addOnSuccessListener {
                                        CustomToast(context, "Account has been created")
                                        navController.popBackStack()
                                    }
                                    .addOnFailureListener {
                                        CustomToast(context, "Failed to create account")
                                    }
                            }
                        }
                        .addOnFailureListener {
                            CustomToast(context, "Error checking existing account")
                        }
                }
            },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(180.dp)
                    .padding(top = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2),
                    contentColor = Color.White
                )
            ) {
                Text("Create Account", fontSize = 16.sp)
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
                    onClick = { navController.popBackStack() },
                    contentPadding = PaddingValues(start = 0.dp)
                ) {
                    Text(
                        text = "Log In",
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
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}

