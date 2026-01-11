package com.example.lojasocial.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.ui.inventory.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryReportScreen(
    nav: NavController,
    vm: InventoryViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        vm.load()
    }

    val grouped = remember(vm.products) {
        vm.products.groupBy { it.category }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventário por Categoria") },
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

        if (grouped.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Sem dados no inventário")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(grouped.entries.toList()) { entry ->
                    CategoryCard(
                        category = entry.key,
                        productCount = entry.value.size,
                        totalQuantity = entry.value.sumOf { it.quantity }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: String,
    productCount: Int,
    totalQuantity: Int
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    Icons.Default.Category,
                    contentDescription = null
                )

                Column {
                    Text(
                        category,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "$productCount produtos",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Text(
                totalQuantity.toString(),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
