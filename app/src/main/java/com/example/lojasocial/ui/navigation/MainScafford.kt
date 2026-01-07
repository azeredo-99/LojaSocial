package com.example.lojasocial.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lojasocial.ui.beneficiaries.*
import com.example.lojasocial.ui.deliveries.AddDeliveryScreen
import com.example.lojasocial.ui.deliveries.DeliveriesHistoryScreen
import com.example.lojasocial.ui.help.HelpScreen
import com.example.lojasocial.ui.home.HomeScreen
import com.example.lojasocial.ui.home.PlaceholderScreen
import com.example.lojasocial.ui.inventory.InventoryScreen
import com.example.lojasocial.ui.products.AddProductScreen
import com.example.lojasocial.ui.profile.ProfileScreen

@Composable
fun MainScaffold(
    rootNavController: NavHostController
) {
    val innerNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(innerNavController) }
    ) { padding ->

        NavHost(
            navController = innerNavController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {

            /* ---------------- HOME ---------------- */
            composable("home") {
                HomeScreen(innerNavController)
            }

            /* ---------------- BENEFICIÁRIOS ---------------- */
            composable("beneficiaries") {
                BeneficiariesScreen(innerNavController)
            }

            composable("addBeneficiary") {
                AddBeneficiaryScreen(
                    nav = innerNavController,
                    vm = hiltViewModel()
                )
            }

            composable("beneficiaryDetail/{id}") { backStack ->
                val id = backStack.arguments?.getString("id") ?: return@composable
                val vm = hiltViewModel<BeneficiariesViewModel>()

                BeneficiaryDetailScreen(
                    nav = innerNavController,
                    vm = vm,
                    beneficiaryId = id
                )
            }

            composable("editBeneficiary/{id}") { backStack ->
                val id = backStack.arguments?.getString("id") ?: return@composable
                val vm = hiltViewModel<BeneficiariesViewModel>()

                LaunchedEffect(Unit) { vm.load() }

                val beneficiary = vm.beneficiaries.firstOrNull { it.id == id }

                if (beneficiary != null) {
                    EditBeneficiaryScreen(
                        nav = innerNavController,
                        vm = vm,
                        beneficiary = beneficiary
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            /* ---------------- ENTREGAS (HISTÓRICO GLOBAL) ---------------- */
            composable("deliveries") {
                DeliveriesHistoryScreen(nav = innerNavController)
            }

            composable("addDelivery") {
                AddDeliveryScreen(nav = innerNavController)
            }

            /* ---------------- INVENTÁRIO (onde ficam os produtos) ---------------- */
            composable("inventory") {
                InventoryScreen(nav = innerNavController)
            }

            // Adicionar Produto (chamado a partir do Inventário)
            composable("addProduct") {
                AddProductScreen(nav = innerNavController)
            }

            /* ---------------- PERFIL ---------------- */
            composable("profile") {
                ProfileScreen(rootNavController)
            }

            /* ---------------- AJUDA ---------------- */
            composable("help") {
                HelpScreen()
            }

            /* ---------------- EM DESENVOLVIMENTO ---------------- */
            composable("donations") { PlaceholderScreen("Doações") }
            composable("schedule") { PlaceholderScreen("Agendamentos") }
            composable("reports") { PlaceholderScreen("Relatórios") }
            composable("alerts") { PlaceholderScreen("Alertas") }
        }
    }
}
