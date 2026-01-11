package com.example.lojasocial.ui.donations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lojasocial.models.Product
import java.util.*

@Composable
fun SelectDonationProductDialog(
    availableProducts: List<Product>,
    alreadyAddedProducts: List<Product>,
    onDismiss: () -> Unit,
    onAdd: (Product) -> Unit
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var quantityReceived by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val categories = listOf(
        "Alimentação",
        "Bebidas",
        "Higiene",
        "Limpeza"
    )

    // Evita duplicados
    val addedIds = alreadyAddedProducts.map { it.id }
    val notAddedProducts = availableProducts.filter { it.id !in addedIds }

    val filteredProducts = selectedCategory?.let { category ->
        notAddedProducts.filter { it.category == category }
    } ?: notAddedProducts

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Produto à Doação") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Text("Filtrar por categoria", style = MaterialTheme.typography.labelMedium)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = {
                            selectedCategory = null
                            selectedProduct = null
                            quantityReceived = ""
                        },
                        label = { Text("Todos") }
                    )

                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                                selectedProduct = null
                                quantityReceived = ""
                            },
                            label = { Text(category) }
                        )
                    }
                }

                if (filteredProducts.isEmpty()) {
                    Text(
                        "Não existem produtos disponíveis",
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(filteredProducts) { product ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedProduct = product
                                            quantityReceived = ""
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor =
                                            if (selectedProduct?.id == product.id)
                                                MaterialTheme.colorScheme.primaryContainer
                                            else
                                                MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(product.name)
                                        Text(
                                            "${product.category} • ${product.unit}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (selectedProduct != null) {
                    OutlinedTextField(
                        value = quantityReceived,
                        onValueChange = {
                            if (it.isEmpty() || it.toIntOrNull() != null) {
                                quantityReceived = it
                            }
                        },
                        label = { Text("Quantidade recebida") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = selectedProduct != null &&
                        quantityReceived.toIntOrNull()?.let { it > 0 } == true,
                onClick = {
                    selectedProduct?.let { product ->
                        val qty = quantityReceived.toInt()
                        onAdd(
                            product.copy(
                                id = UUID.randomUUID().toString(),
                                quantity = qty
                            )
                        )
                    }
                }
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
