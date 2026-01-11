package com.example.lojasocial.ui.reports

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.ui.beneficiaries.BeneficiariesViewModel
import com.example.lojasocial.ui.deliveries.DeliveriesViewModel
import com.example.lojasocial.ui.donations.DonationsViewModel
import com.example.lojasocial.ui.inventory.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    nav: NavController,
    beneficiariesVm: BeneficiariesViewModel = hiltViewModel(),
    inventoryVm: InventoryViewModel = hiltViewModel(),
    deliveriesVm: DeliveriesViewModel = hiltViewModel(),
    donationsVm: DonationsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        beneficiariesVm.load()
        inventoryVm.load()
        deliveriesVm.load()
        donationsVm.load()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatórios") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /* ---------- BENEFICIÁRIOS ---------- */
            ReportCard(
                title = "Beneficiários",
                subtitle = "${beneficiariesVm.beneficiaries.size} beneficiários registados",
                icon = Icons.Default.People,
                onClick = { nav.navigate("beneficiariesReport") }
            )

            /* ---------- INVENTÁRIO ---------- */
            ReportCard(
                title = "Inventário",
                subtitle = "${inventoryVm.products.size} produtos registados",
                icon = Icons.Default.Inventory,
                onClick = { nav.navigate("inventoryReport") }
            )

            /* ---------- ENTREGAS ---------- */
            ReportCard(
                title = "Entregas",
                subtitle = "${deliveriesVm.deliveries.size} entregas registadas",
                icon = Icons.Default.LocalShipping,
                onClick = { nav.navigate("deliveriesReport") }
            )

            /* ---------- DOAÇÕES ---------- */
            ReportCard(
                title = "Doações",
                subtitle = "${donationsVm.donations.size} doações registadas",
                icon = Icons.Default.VolunteerActivism,
                onClick = { nav.navigate("donationsReport") }
            )
        }
    }
}

/* ---------- CARD REUTILIZÁVEL ---------- */
@Composable
private fun ReportCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(icon, contentDescription = null)

            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
