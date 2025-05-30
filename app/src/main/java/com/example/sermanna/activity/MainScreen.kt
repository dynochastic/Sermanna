@file:Suppress("DEPRECATION")

package com.example.sermanna.activity

import PrivateChatScreen
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sermanna.R
import com.example.sermanna.components.BottomNavBar
import com.example.sermanna.components.Screens
import com.example.sermanna.components.TopNav
import com.example.sermanna.screens.ViewProfileScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    profileImageRes: Int,
    drawerState: DrawerState,
    firstName: String,
    lastName: String,
    userEmail: String,
    password: String,
    onLogOut: () -> Unit
) {
    val navController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                Modifier.background(Color.White).width(280.dp)
            ) {
                Text(
                    text = "More",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()
            }
        }
    ) {
        Scaffold(
            topBar = { TopNav(drawerState) },
            bottomBar = {
                BottomNavBar(profileImageRes = profileImageRes) { destination ->
                    navController.navigate(destination) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        ) { innerPadding ->
            AnimatedNavHost(
                navController = navController,
                startDestination = Screens.Home.route,
                modifier = Modifier.padding(innerPadding),


            ) {
                composable(Screens.Home.route) { HomeScreen() }

                composable(Screens.Search.route) {
                    SearchAccountsScreen { account ->
                        navController.navigate("viewProfile/${account.Email}")
                    }
                }

                composable(Screens.Messages.route) {
                    MessageSearchScreen { account ->
                        navController.navigate("privateChat/${userEmail}/${account.Email}")
                    }

                }


                composable(Screens.Profile.route) {
                    ProfileScreen(
                        firstName = firstName,
                        lastName = lastName,
                        userEmail = userEmail,
                        password = password,
                        navController = navController,
                        onLogOut = onLogOut
                    )
                }

                composable(
                    route = "editAccount/{firstName}/{lastName}/{userEmail}/{password}",
                    arguments = listOf(
                        navArgument("firstName") { type = NavType.StringType },
                        navArgument("lastName") { type = NavType.StringType },
                        navArgument("userEmail") { type = NavType.StringType },
                        navArgument("password") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
                    val lastName = backStackEntry.arguments?.getString("lastName") ?: ""
                    val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
                    val password = backStackEntry.arguments?.getString("password") ?: ""

                    EditAccount(
                        firstNameInitial = firstName,
                        lastNameInitial = lastName,
                        userEmail = userEmail,
                        password = password,
                        onBack = { navController.popBackStack() },
                        onSave = { updatedFirstName, updatedLastName ->
                            navController.popBackStack()
                        }
                    )
                }
                composable(
                    route = "privateChat/{senderEmail}/{receiverEmail}",
                    arguments = listOf(
                        navArgument("senderEmail") { type = NavType.StringType },
                        navArgument("receiverEmail") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val senderEmail = backStackEntry.arguments?.getString("senderEmail") ?: ""
                    val receiverEmail = backStackEntry.arguments?.getString("receiverEmail") ?: ""

                    PrivateChatScreen(
                        senderEmail = senderEmail,
                        receiverEmail = receiverEmail,
                        onBack = { navController.popBackStack() },
                        onViewProfile = { email ->
                            navController.navigate("viewProfile/$email")
                        }
                    )
                }
                composable(Screens.Messages.route) {
                    MessageSearchScreen { account ->
                        navController.navigate("privateChat/${userEmail}/${account.Email}")
                    }
                }

                composable(
                    route = "viewProfile/{email}",
                    arguments = listOf(navArgument("email") { type = NavType.StringType })
                ) { backStackEntry ->
                    val email = backStackEntry.arguments?.getString("email") ?: ""
                    ViewProfileScreen(
                        email = email,
                        lastname = lastName,
                        firstname = firstName,
                        onBack = { navController.popBackStack() }
                    )
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(
        profileImageRes = R.drawable.appliances,
        drawerState = rememberDrawerState(DrawerValue.Closed),
        firstName = "Taper",
        lastName = "Fade",
        userEmail = "ninjalowtaperfade@gmail.com",
        password = "imagine",
        onLogOut = {}
    )
}
