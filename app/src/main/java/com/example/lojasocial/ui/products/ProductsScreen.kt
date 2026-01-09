package com.example.lojasocial.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.models.Product
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    nav: NavController,
    vm: ProductsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        vm.load()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Produtos") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {

                    if (vm.hasExpiryAlerts()) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(
                                        text = "${
                                            vm.getExpiredCount() +
                                                    vm.getExpiringSoonCount()
                                        }"
                                    )
                                }
                            }
                        ) {
                            IconButton(onClick = { /* TODO: navegar para alertas */ }) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Alertas de validade",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    IconButton(onClick = { nav.navigate("add_product") }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar produto"
                        )
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

            vm.products.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum produto registado")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vm.products) { product ->
                        ProductCard(
                            product = product,
                            onDelete = { vm.removeProduct(product.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onDelete: () -> Unit
) {
    val cardColors = when {
        product.isExpired() -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
        product.isExpiringSoon() -> CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3E0)
        )
        else -> CardDefaults.cardColors()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = cardColors
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Categoria: ${product.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Unidade: ${product.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                product.expireDate?.let { timestamp ->
                    val calendar = Calendar.getInstance().apply {
                        timeInMillis = timestamp
                    }

                    val dateStr =
                        "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
                                "${calendar.get(Calendar.MONTH) + 1}/" +
                                calendar.get(Calendar.YEAR)

                    val validityText = when {
                        product.isExpired() ->
                            "Vencido em $dateStr"

                        product.isExpiringSoon() ->
                            "Vence em ${product.daysUntilExpiry()} dia(s) - $dateStr"

                        else ->
                            "Validade: $dateStr"
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        if (product.isExpired() || product.isExpiringSoon()) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (product.isExpired())
                                    MaterialTheme.colorScheme.error
                                else
                                    Color(0xFFFF9800)
                            )
                        }

                        Text(
                            text = validityText,
                            style = MaterialTheme.typography.bodySmall,
                            color = when {
                                product.isExpired() ->
                                    MaterialTheme.colorScheme.error
                                product.isExpiringSoon() ->
                                    Color(0xFFFF9800)
                                else ->
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remover",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
