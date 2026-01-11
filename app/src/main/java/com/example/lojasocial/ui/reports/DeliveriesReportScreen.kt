package com.example.lojasocial.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.ui.deliveries.DeliveriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveriesReportScreen(
    nav: NavController,
    vm: DeliveriesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        vm.load()
    }

    val totalDeliveries = vm.deliveries.size
    val delivered = vm.deliveries.count { it.state }
    val reserved = vm.deliveries.count { !it.state }
    val totalItems = vm.deliveries.sumOf { it.items.size }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de Entregas") },
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
                    title = "Total de Entregas",
                    value = totalDeliveries.toString(),
                    icon = Icons.Default.LocalShipping
                )

                ReportStatCard(
                    title = "Entregas Realizadas",
                    value = delivered.toString(),
                    icon = Icons.Default.CheckCircle
                )

                ReportStatCard(
                    title = "Entregas Reservadas",
                    value = reserved.toString(),
                    icon = Icons.Default.Schedule
                )

                ReportStatCard(
                    title = "Total de Produtos Entregues",
                    value = totalItems.toString(),
                    icon = Icons.Default.LocalShipping
                )
            }
        }
    }
}

@Composable
fun ReportStatCard(
    title: String,
    value: String,
    icon: ImageVector
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null)
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Text(
                value,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
