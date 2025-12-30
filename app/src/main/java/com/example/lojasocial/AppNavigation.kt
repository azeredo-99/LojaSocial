package com.example.lojasocial

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lojasocial.ui.login.*
import com.example.lojasocial.ui.navigation.MainScaffold

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            val vm = hiltViewModel<AuthViewModel>()
            LoginScreen(navController, vm)
        }

        composable("register") {
            val vm = hiltViewModel<AuthViewModel>()
            RegisterScreen(navController, vm)
        }

        composable("recover") {
            val vm = hiltViewModel<AuthViewModel>()
            RecoverScreen(navController, vm)
        }


        composable("main") {
            MainScaffold(navController)
        }
    }
}
