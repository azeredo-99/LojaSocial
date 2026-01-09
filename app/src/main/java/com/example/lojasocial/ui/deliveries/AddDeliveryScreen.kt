package com.example.lojasocial.ui.deliveries

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.models.Delivery
import com.example.lojasocial.models.Product
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeliveryScreen(
    nav: NavController,
    vm: DeliveriesViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isDelivered by remember { mutableStateOf(false) }

    // Lista de produtos da entrega
    var productsList by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Dialog para adicionar produto
    var showAddProductDialog by remember { mutableStateOf(false) }

    val date = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Entrega") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome do beneficiário") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = number,
                onValueChange = { number = it },
                label = { Text("Nº estudante") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = course,
                onValueChange = { course = it },
                label = { Text("Curso") },
                modifier = Modifier.fillMaxWidth()
            )

            // Estado da entrega
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDelivered)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Estado da entrega",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = if (isDelivered) "Entregue" else "Não entregue",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDelivered)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = isDelivered,
                        onCheckedChange = { isDelivered = it }
                    )
                }
            }

            // Seção de produtos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Produtos (${productsList.size})",
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = { showAddProductDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar produto")
                }
            }

            // Lista de produtos adicionados
            if (productsList.isEmpty()) {
                Text(
                    text = "Nenhum produto adicionado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(productsList) { product ->
                            ProductItem(
                                product = product,
                                onRemove = {
                                    productsList = productsList.filter { it.id != product.id }
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Observações") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    vm.addDelivery(
                        Delivery(
                            id = UUID.randomUUID().toString(),
                            beneficiaryName = name,
                            studentNumber = number,
                            course = course,
                            date = date,
                            state = isDelivered,
                            items = productsList,
                            notes = notes
                        )
                    )
                    nav.popBackStack()
                },
                enabled = name.isNotBlank() && number.isNotBlank() && productsList.isNotEmpty()
            ) {
                Text("Guardar entrega")
            }
        }
    }

    // Dialog para adicionar produto
    if (showAddProductDialog) {
        AddProductDialog(
            onDismiss = { showAddProductDialog = false },
            onAdd = { product ->
                productsList = productsList + product
                showAddProductDialog = false
            }
        )
    }
}

@Composable
fun ProductItem(
    product: Product,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${product.quantity} ${product.unit} • ${product.category}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remover produto",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddProductDialog(
    onDismiss: () -> Unit,
    onAdd: (Product) -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var productUnit by remember { mutableStateOf("unidades") }
    var productCategory by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Produto") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Nome do produto") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = productQuantity,
                        onValueChange = { productQuantity = it },
                        label = { Text("Quantidade") },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = productUnit,
                        onValueChange = { productUnit = it },
                        label = { Text("Unidade") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = productCategory,
                    onValueChange = { productCategory = it },
                    label = { Text("Categoria") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (productName.isNotBlank() && productQuantity.isNotBlank()) {
                        onAdd(
                            Product(
                                id = UUID.randomUUID().toString(),
                                name = productName,
                                quantity = productQuantity.toIntOrNull() ?: 0,
                                unit = productUnit,
                                category = productCategory
                            )
                        )
                    }
                },
                enabled = productName.isNotBlank() && productQuantity.isNotBlank()
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

