package com.example.lojasocial.ui.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.models.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    nav: NavController,
    productId: String,
    vm: InventoryViewModel = hiltViewModel()
) {
    LaunchedEffect(productId) {
        vm.loadById(productId)
    }

    val product = vm.selectedProduct

    var name by remember(product?.id) { mutableStateOf(product?.name ?: "") }
    var quantity by remember(product?.id) { mutableStateOf((product?.quantity ?: 0).toString()) }
    var unit by remember(product?.id) { mutableStateOf(product?.unit ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Produto") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->

        when {
            vm.isLoading && product == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            product == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(vm.errorMessage ?: "Produto não encontrado")
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it.filter { c -> c.isDigit() } },
                        label = { Text("Quantidade") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text("Unidade (ex: kg, un, L)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    vm.errorMessage?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }

                    Button(
                        onClick = {
                            val updated = Product(
                                id = product.id,
                                name = name.trim(),
                                quantity = quantity.toIntOrNull() ?: 0,
                                unit = unit.trim()
                            )

                            vm.update(updated) {
                                nav.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = name.isNotBlank()
                    ) {
                        Text("Guardar alterações")
                    }
                }
            }
        }
    }
}
