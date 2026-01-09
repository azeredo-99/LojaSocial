package com.example.lojasocial.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lojasocial.ui.beneficiaries.*
import com.example.lojasocial.ui.deliveries.AddDeliveryScreen
import com.example.lojasocial.ui.deliveries.DeliveriesHistoryScreen
import com.example.lojasocial.ui.help.HelpScreen
import com.example.lojasocial.ui.home.HomeScreen
import com.example.lojasocial.ui.home.PlaceholderScreen
import com.example.lojasocial.ui.inventory.EditProductScreen
import com.example.lojasocial.ui.inventory.InventoryScreen
import com.example.lojasocial.ui.profile.ProfileScreen
import com.example.lojasocial.ui.products.AddProductScreen
import com.example.lojasocial.ui.components.IpcaTopBar   // ✅ adiciona isto

@Composable
fun MainScaffold(
    rootNavController: NavHostController
) {
    val innerNavController = rememberNavController()

    // ✅ saber em que rota estás
    val backStackEntry by innerNavController.currentBackStackEntryAsState()
    val route = backStackEntry?.destination?.route.orEmpty()

    // ✅ título por rota (podes ajustar os nomes)
    val title = when {
        route == "home" -> "Loja Social"
        route == "beneficiaries" -> "Beneficiários"
        route.startsWith("beneficiaryDetail") -> "Beneficiário"
        route.startsWith("editBeneficiary") -> "Editar Beneficiário"
        route == "deliveries" -> "Entregas"
        route == "inventory" -> "Inventário"
        route.startsWith("editProduct") -> "Editar Produto"
        route == "profile" -> "Perfil"
        route == "help" -> "Ajuda"
        route == "donations" -> "Doações"
        route == "schedule" -> "Agendamentos"
        route == "reports" -> "Relatórios"
        route == "alerts" -> "Alertas"
        else -> "Loja Social"
    }

    // ✅ rotas onde NÃO queres topBar global (porque esses screens já têm topbar com back)
    val hideTopBar = route == "addBeneficiary" || route == "addDelivery" || route == "addProduct"

    Scaffold(
        topBar = {
            if (!hideTopBar) {
                IpcaTopBar(title = title)
            }
        },
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

            /* ---------------- INVENTÁRIO ---------------- */
            composable("inventory") {
                InventoryScreen(nav = innerNavController)
            }

            composable("addProduct") {
                AddProductScreen(nav = innerNavController)
            }

            composable("editProduct/{id}") { backStack ->
                val id = backStack.arguments?.getString("id") ?: return@composable

                EditProductScreen(
                    nav = innerNavController,
                    productId = id
                )
            }

            /* ---------------- PERFIL ---------------- */
            composable("profile") {
                ProfileScreen(rootNavController)
            }

            /* ---------------- AJUDA ---------------- */
            composable("help") {
                HelpScreen()
            }

            /* ---------------- PLACEHOLDERS ---------------- */
            composable("donations") { PlaceholderScreen("Doações") }
            composable("schedule") { PlaceholderScreen("Agendamentos") }
            composable("reports") { PlaceholderScreen("Relatórios") }
            composable("alerts") { PlaceholderScreen("Alertas") }
        }
    }
}
