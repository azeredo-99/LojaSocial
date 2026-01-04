package com.example.lojasocial

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lojasocial.ui.login.*
import com.example.lojasocial.ui.navigation.MainScaffold
import com.example.lojasocial.ui.profile.ChangePasswordScreen
import com.example.lojasocial.ui.profile.EditProfileScreen
import com.example.lojasocial.ui.profile.ProfileViewModel

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        /* ---------- AUTH ---------- */
        composable("login") {
            LoginScreen(
                nav = navController,
                vm = hiltViewModel()
            )
        }

        composable("register") {
            RegisterScreen(
                nav = navController,
                vm = hiltViewModel()
            )
        }

        composable("recover") {
            RecoverScreen(
                nav = navController,
                vm = hiltViewModel()
            )
        }

        /* ---------- MAIN  ---------- */
        composable("main") {
            MainScaffold(
                rootNavController = navController
            )
        }

        /* ---------- PROFILE  ---------- */
        composable("editProfile") {
            EditProfileScreen(
                nav = navController,
                vm = hiltViewModel<ProfileViewModel>()
            )
        }

        composable("changePassword") {
            ChangePasswordScreen(
                nav = navController,
                vm = hiltViewModel<ProfileViewModel>()
            )
        }
    }
}
