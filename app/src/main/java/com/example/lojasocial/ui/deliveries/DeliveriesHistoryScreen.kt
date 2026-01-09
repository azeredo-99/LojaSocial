package com.example.lojasocial.ui.deliveries

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.models.Delivery

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveriesHistoryScreen(
    nav: NavController,
    vm: DeliveriesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        vm.load()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Entregas") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { nav.navigate("addDelivery") }) {
                        Icon(Icons.Default.Add, contentDescription = "Nova entrega")
                    }
                }
            )
        }
    ) { padding ->

        when {
            vm.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            vm.deliveries.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ainda não existem entregas registadas")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(vm.deliveries) { delivery ->
                        DeliveryHistoryItem(delivery)
                    }
                }
            }
        }
    }
}

@Composable
private fun DeliveryHistoryItem(delivery: Delivery) {
    val totalUnits = delivery.items.sumOf { it.quantity }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = delivery.beneficiaryName,
                style = MaterialTheme.typography.titleMedium
            )

            Text("Nº estudante: ${delivery.studentNumber}")
            Text("Curso: ${delivery.course}")

            Divider()

            Text("Produtos: ${delivery.items.size}")
            Text("Total de unidades: $totalUnits")
            Text("Data: ${delivery.date}")

            Text(
                text = if (delivery.state) "Estado: Entregue" else "Estado: Não entregue",
                color = if (delivery.state)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )

            if (delivery.notes.isNotBlank()) {
                Divider()
                Text(
                    delivery.notes,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
