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
import com.example.lojasocial.ui.student.StudentStatusScreen

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

        /* ---------- LOGIN (COLABORADOR) ---------- */
        composable("login") {
            LoginScreen(
                nav = navController,
                vm = hiltViewModel(),
                successRoute = "main",
                title = "Login do colaborador",
                showRegisterOption = false,
                signOutOnBack = false
            )
        }

        /* ---------- LOGIN (ESTUDANTE) ---------- */
        composable("loginStudent") {
            LoginScreen(
                nav = navController,
                vm = hiltViewModel(),
                successRoute = "studentStatus",
                title = "Login do estudante",
                showRegisterOption = true,
                signOutOnBack = true
            )
        }

        /* ---------- AUTH ---------- */
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

        /* ---------- ESTADO DA CANDIDATURA (ESTUDANTE) ---------- */
        composable("studentStatus") {
            StudentStatusScreen(nav = navController)
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
