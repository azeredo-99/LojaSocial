package com.example.lojasocial.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
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
import com.example.lojasocial.ui.deliveries.*
import com.example.lojasocial.ui.donations.*
import com.example.lojasocial.ui.help.HelpScreen
import com.example.lojasocial.ui.home.HomeScreen
import com.example.lojasocial.ui.inventory.InventoryScreen
import com.example.lojasocial.ui.products.AddProductScreen
import com.example.lojasocial.ui.profile.ProfileScreen
import com.example.lojasocial.ui.reports.*
import com.example.lojasocial.ui.reports.BeneficiariesReportScreen

@Composable
fun MainScaffold(
    rootNavController: NavHostController
) {
    val innerNavController = rememberNavController()

    Scaffold(
        topBar = { TopBar() },
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

            /* ---------------- ENTREGAS ---------------- */
            composable("deliveries") {
                DeliveriesHistoryScreen(nav = innerNavController)
            }

            composable("addDelivery") {
                AddDeliveryScreen(nav = innerNavController)
            }

            composable("editDelivery/{deliveryId}") { backStack ->
                val deliveryId =
                    backStack.arguments?.getString("deliveryId") ?: return@composable

                EditDeliveryScreen(
                    deliveryId = deliveryId,
                    nav = innerNavController
                )
            }

            /* ---------------- INVENTÁRIO ---------------- */
            composable("inventory") {
                InventoryScreen(nav = innerNavController)
            }

            composable("addProduct") {
                AddProductScreen(nav = innerNavController)
            }

            /* ---------------- DOAÇÕES ---------------- */
            composable("donations") {
                DonationsScreen(nav = innerNavController)
            }

            composable("addDonation") {
                AddDonationScreen(nav = innerNavController)
            }

            /* ---------------- RELATÓRIOS ---------------- */
            composable("reports") {
                ReportsScreen(nav = innerNavController)
            }

            composable("inventoryReport") {
                InventoryReportScreen(nav = innerNavController)
            }


            composable("deliveriesReport") {
                DeliveriesReportScreen(nav = innerNavController)
            }

            composable("donationsReport") {
                DonationsReportScreen(nav = innerNavController)
            }

            composable("beneficiariesReport") {
                BeneficiariesReportScreen(nav = innerNavController)
            }


            /* ---------------- PERFIL ---------------- */
            composable("profile") {
                ProfileScreen(rootNavController)
            }

            /* ---------------- AJUDA ---------------- */
            composable("help") {
                HelpScreen()
            }

            /* ---------------- ALERTAS ---------------- */
            composable("alerts") {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Alertas em desenvolvimento")
                }
            }
        }
    }
}
