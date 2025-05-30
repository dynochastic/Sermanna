package com.example.sermanna.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sermanna.R
import com.example.sermanna.ui.theme.SermannaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SermannaTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            onNavigateToHomeScreen = { firstName, lastName, userEmail, password ->
                                navController.navigate("main/$firstName/$lastName/$userEmail/$password") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("signup") {
                        SignUpScreen(navController = navController)
                    }
                    composable("main/{firstName}/{lastName}/{userEmail}/{password}") { backStackEntry ->
                        val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
                        val lastName = backStackEntry.arguments?.getString("lastName") ?: ""
                        val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
                        val password = backStackEntry.arguments?.getString("password") ?: ""
                        MainScreen(
                            profileImageRes = R.drawable.user1,
                            drawerState = rememberDrawerState(DrawerValue.Closed),
                            firstName = firstName,
                            lastName = lastName,
                            userEmail = userEmail,
                            password = password,
                            onLogOut = {
                                navController.navigate("login") {
                                    popUpTo("main/$firstName/$lastName/$userEmail/$password") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}