package com.example.lojasocial.ui.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
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
fun InventoryScreen(
    nav: NavController,
    vm: InventoryViewModel = hiltViewModel()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(Unit) {
        vm.load()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventário") },
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
                                    Text("${vm.getExpiredCount() + vm.getExpiringSoonCount()}")
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

                    IconButton(onClick = { nav.navigate("addProduct") }) {
                        Icon(
                            Icons.Default.Add,
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(vm.products) { product ->
                        InventoryProductCard(
                            product = product,
                            onDelete = {
                                productToDelete = product
                                showDeleteDialog = true
                            },
                            onIncrement = {
                                vm.updateProduct(product.copy(quantity = product.quantity + 1))
                            },
                            onDecrement = {
                                if (product.quantity > 0) {
                                    vm.updateProduct(product.copy(quantity = product.quantity - 1))
                                }
                            }
                        )
                    }
                }
            }
        }

        // Dialog de confirmação de exclusão
        if (showDeleteDialog && productToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                icon = {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = { Text("Remover produto") },
                text = { Text("Tem certeza que deseja remover '${productToDelete?.name}'?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            productToDelete?.let { vm.removeProduct(it.id) }
                            showDeleteDialog = false
                            productToDelete = null
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Remover")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        productToDelete = null
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun InventoryProductCard(
    product: Product,
    onDelete: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cabeçalho com nome e botão de apagar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remover produto",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            HorizontalDivider()

            // Informações do produto
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    InfoRow(label = "Categoria", value = product.category)
                    InfoRow(label = "Unidade", value = product.unit)

                    // Informação de validade
                    product.expireDate?.let { timestamp ->
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = timestamp
                        }
                        val dateStr = "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
                            "${calendar.get(Calendar.MONTH) + 1}/" +
                            calendar.get(Calendar.YEAR)

                        val (label, value, color) = when {
                            product.isExpired() -> Triple(
                                "Expirou em",
                                dateStr,
                                MaterialTheme.colorScheme.error
                            )
                            product.isExpiringSoon() -> Triple(
                                "Vence em ${product.daysUntilExpiry()} dia(s)",
                                dateStr,
                                Color(0xFFFF9800)
                            )
                            else -> Triple(
                                "Validade",
                                dateStr,
                                MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (product.isExpired() || product.isExpiringSoon()) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = color
                                )
                            }
                            Column {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = value,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = color
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            // Controlo de stock
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Stock",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botão diminuir
                    FilledTonalIconButton(
                        onClick = onDecrement,
                        enabled = product.quantity > 0
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Diminuir quantidade"
                        )
                    }

                    // Quantidade
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "${product.quantity}",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    // Botão aumentar
                    FilledTonalIconButton(onClick = onIncrement) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar quantidade"
                        )
                    }
                }
            }

            // Alerta de stock baixo
            if (product.quantity < 5 && product.quantity > 0) {
                Surface(
                    color = Color(0xFFFFF3E0),
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Stock baixo!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFF9800)
                        )
                    }
                }
            } else if (product.quantity == 0) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Produto esgotado!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
