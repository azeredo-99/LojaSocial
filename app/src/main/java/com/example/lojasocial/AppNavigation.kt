package com.example.lojasocial

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lojasocial.ui.entry.EntryScreen
import com.example.lojasocial.ui.login.*
import com.example.lojasocial.ui.navigation.MainScaffold
import com.example.lojasocial.ui.profile.ChangePasswordScreen
import com.example.lojasocial.ui.profile.EditProfileScreen
import com.example.lojasocial.ui.profile.ProfileViewModel
import com.example.lojasocial.ui.student.ApplicationSubmittedScreen
import com.example.lojasocial.ui.student.StudentApplicationScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "entry"
    ) {

        /* ---------- ENTRY (ANTES DO LOGIN) ---------- */
        composable("entry") {
            EntryScreen(nav = navController)
        }

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

        /* ---------- CANDIDATURA (ESTUDANTE) ---------- */
        composable("studentApplication") {
            StudentApplicationScreen(nav = navController)
        }

        /* ---------- CONFIRMAÇÃO DA CANDIDATURA ---------- */
        composable("applicationSubmitted") {
            ApplicationSubmittedScreen(nav = navController)
        }

        /* ---------- MAIN ---------- */
        composable("main") {
            MainScaffold(rootNavController = navController)
        }

        /* ---------- PROFILE ---------- */
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
