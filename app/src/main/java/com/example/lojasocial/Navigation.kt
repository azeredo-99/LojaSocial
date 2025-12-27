package com.example.lojasocial

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.lojasocial.ui.home.HomeScreen
import com.example.lojasocial.ui.home.PlaceholderScreen
import com.example.lojasocial.ui.login.*

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

        composable("home") {
            HomeScreen(navController)
        }

        // 🔹 ROTAS DO HOME (ligadas aos ícones)
        composable("beneficiaries") {
            PlaceholderScreen("Beneficiários")
        }

        composable("inventory") {
            PlaceholderScreen("Inventário")
        }

        composable("donations") {
            PlaceholderScreen("Doações")
        }

        composable("deliveries") {
            PlaceholderScreen("Entregas")
        }

        composable("schedule") {
            PlaceholderScreen("Agendamentos")
        }

        composable("reports") {
            PlaceholderScreen("Relatórios")
        }

        composable("alerts") {
            PlaceholderScreen("Alertas")
        }
    }
}
