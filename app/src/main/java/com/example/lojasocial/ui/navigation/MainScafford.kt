package com.example.lojasocial.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.rememberNavController
import com.example.lojasocial.ui.home.HomeScreen
import com.example.lojasocial.ui.home.PlaceholderScreen
import com.example.lojasocial.ui.profile.ProfileScreen
import com.example.lojasocial.ui.help.HelpScreen

@Composable
fun MainScaffold(rootNavController: NavHostController) {

    // NavController INTERNO
    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(innerNavController) }
    ) { padding ->

        NavHost(
            navController = innerNavController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {

            composable("home") {
                HomeScreen(innerNavController)
            }

            composable("profile") {
                ProfileScreen(rootNavController)
            }

            composable("help") {
                HelpScreen()
            }

            composable("beneficiaries") { PlaceholderScreen("Beneficiários") }
            composable("inventory") { PlaceholderScreen("Inventário") }
            composable("donations") { PlaceholderScreen("Doações") }
            composable("deliveries") { PlaceholderScreen("Entregas") }
            composable("schedule") { PlaceholderScreen("Agendamentos") }
            composable("reports") { PlaceholderScreen("Relatórios") }
            composable("alerts") { PlaceholderScreen("Alertas") }
        }
    }
}

