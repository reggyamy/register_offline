package com.reggya.registeroffline.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reggya.registeroffline.presentation.ui.screen.AddMemberScreen
import com.reggya.registeroffline.presentation.ui.viewmodel.UserViewModel
import com.reggya.registeroffline.presentation.ui.screen.LoginScreen
import com.reggya.registeroffline.presentation.ui.screen.ProfileScreen
import com.reggya.registeroffline.presentation.ui.screen.home.HomeScreen
import com.reggya.registeroffline.presentation.ui.screen.splashscreen.SplashScreen

object Routes {
    const val LOGIN   = "login"
    const val HOME    = "home"
    const val ADD_MEMBER  = "add_member"
    const val EDIT_MEMBER = "edit_member/{memberId}"
    const val PROFILE = "profile"

    fun editMember(memberId: Long) = "edit_member/$memberId"
}

@Composable
fun RegisterOfflineApp() {
    val navController = rememberNavController()

    val userViewModel: UserViewModel = hiltViewModel()
    val isLoggedIn by userViewModel.isLoggedIn.collectAsState()

    if (isLoggedIn == null){
        SplashScreen()
        return
    }

    val startDestination = if (isLoggedIn == true)  Routes.HOME else Routes.LOGIN


    NavHost(
        modifier = Modifier.Companion
            .fillMaxSize()
            .navigationBarsPadding(),
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onAddMember = {
                    navController.navigate(Routes.ADD_MEMBER)
                },
                onEditMember = { memberId ->
                    navController.navigate(Routes.editMember(memberId))
                },
                onUploadMember = { memberId ->

                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        composable(Routes.ADD_MEMBER) {
            AddMemberScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Routes.EDIT_MEMBER) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getString("memberId")?.toLongOrNull() ?: 0L
            AddMemberScreen(
                memberId = memberId,
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}