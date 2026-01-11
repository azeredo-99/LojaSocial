package com.example.lojasocial.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.ui.donations.DonationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationsReportScreen(
    nav: NavController,
    vm: DonationsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        vm.load()
    }

    val totalDonations = vm.donations.size
    val totalItems = vm.donations.sumOf { it.items.size }
    val receivedDonations = vm.donations.count { it.received }

    val lastDonationDate = vm.donations
        .maxByOrNull { it.date }
        ?.date ?: "-"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de Doações") },
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

        if (vm.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ReportStatCard(
                    title = "Total de Doações",
                    value = totalDonations.toString(),
                    icon = Icons.Default.VolunteerActivism
                )

                ReportStatCard(
                    title = "Produtos Recebidos",
                    value = totalItems.toString(),
                    icon = Icons.Default.Inventory
                )

                ReportStatCard(
                    title = "Doações Recebidas",
                    value = receivedDonations.toString(),
                    icon = Icons.Default.VolunteerActivism
                )

                ReportStatCard(
                    title = "Última Doação",
                    value = lastDonationDate,
                    icon = Icons.Default.Event
                )
            }
        }
    }
}
