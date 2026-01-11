package com.example.lojasocial.ui.deliveries

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.models.Delivery
import com.example.lojasocial.models.Product
import com.example.lojasocial.repository.BeneficiaryRepository
import com.example.lojasocial.repository.ProductRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeliveryScreen(
    nav: NavController,
    vm: DeliveriesViewModel = hiltViewModel()
) {
    var number by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Auto-fill state
    var isLoadingBeneficiary by remember { mutableStateOf(false) }

    // Lista de produtos da entrega
    var deliveryProducts by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Available products from inventory
    var availableProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoadingProducts by remember { mutableStateOf(true) }

    // Dialog para adicionar produto
    var showAddProductDialog by remember { mutableStateOf(false) }

    // Date picker state
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().timeInMillis) }

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val formattedDate = dateFormatter.format(Date(selectedDate))

    val scope = rememberCoroutineScope()

    // Load available products on first composition
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                availableProducts = ProductRepository.getAll()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoadingProducts = false
            }
        }
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp), // Space for button
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = number,
                        onValueChange = { newNumber ->
                            number = newNumber
                            // Auto-fill beneficiary info when student number changes
                            if (newNumber.isNotBlank()) {
                                scope.launch {
                                    isLoadingBeneficiary = true
                                    try {
                                        val beneficiaries = BeneficiaryRepository.getAll()
                                        val matchingBeneficiary = beneficiaries.find {
                                            it.studentNumber == newNumber
                                        }

                                        if (matchingBeneficiary != null) {
                                            name = matchingBeneficiary.name
                                            course = matchingBeneficiary.course
                                        } else {
                                            // Clear fields if no match
                                            name = ""
                                            course = ""
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    } finally {
                                        isLoadingBeneficiary = false
                                    }
                                }
                            } else {
                                name = ""
                                course = ""
                            }
                        },
                        label = { Text("Nº estudante") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (isLoadingBeneficiary) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    )
                }

                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome do beneficiário") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = number.isNotBlank()
                    )
                }

                item {
                    OutlinedTextField(
                        value = course,
                        onValueChange = { course = it },
                        label = { Text("Curso") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = number.isNotBlank()
                    )
                }

                item {
                    OutlinedTextField(
                        value = formattedDate,
                        onValueChange = { },
                        label = { Text("Data da entrega") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.CalendarToday, contentDescription = "Selecionar data")
                            }
                        }
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Produtos (${deliveryProducts.size})",
                            style = MaterialTheme.typography.titleMedium
                        )

                        IconButton(
                            onClick = { showAddProductDialog = true },
                            enabled = !isLoadingProducts
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Adicionar produto")
                        }
                    }
                }

                if (deliveryProducts.isEmpty()) {
                    item {
                        Text(
                            text = "Nenhum produto adicionado",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(deliveryProducts) { product ->
                        DeliveryProductItem(
                            product = product,
                            onRemove = {
                                deliveryProducts = deliveryProducts.filter { it.id != product.id }
                            }
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Observações") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            }

            // Fixed button at bottom
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                shadowElevation = 8.dp
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    onClick = {
                        vm.addDelivery(
                            Delivery(
                                id = UUID.randomUUID().toString(),
                                beneficiaryName = name,
                                studentNumber = number,
                                course = course,
                                date = formattedDate,
                                state = false, // Default to "Não Entregue"
                                items = deliveryProducts,
                                notes = notes
                            )
                        )
                        nav.popBackStack()
                    },
                    enabled = name.isNotBlank() && number.isNotBlank() && deliveryProducts.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    )
                ) {
                    Text("Guardar entrega")
                }
            }
        }
    }

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = it
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Dialog para selecionar produto do inventário
    if (showAddProductDialog) {
        SelectProductDialog(
            availableProducts = availableProducts,
            alreadyAddedProducts = deliveryProducts,
            onDismiss = { showAddProductDialog = false },
            onAdd = { product ->
                deliveryProducts = deliveryProducts + product
                showAddProductDialog = false
            }
        )
    }
}

@Composable
fun DeliveryProductItem(
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
fun SelectProductDialog(
    availableProducts: List<Product>,
    alreadyAddedProducts: List<Product>,
    onDismiss: () -> Unit,
    onAdd: (Product) -> Unit
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var quantityToDeliver by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // Categories
    val categories = listOf(
        "Alimentação",
        "Bebidas",
        "Higiene",
        "Limpeza"
    )

    // Filter out products already added
    val alreadyAddedIds = alreadyAddedProducts.map { it.id }
    val productsNotAdded = availableProducts.filter { it.id !in alreadyAddedIds }

    // Filter by category if one is selected
    val selectableProducts = if (selectedCategory != null) {
        productsNotAdded.filter { it.category == selectedCategory }
    } else {
        productsNotAdded
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecionar Produto") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Category filter chips
                Text(
                    text = "Filtrar por categoria:",
                    style = MaterialTheme.typography.labelMedium
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // "All" filter chip
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = {
                            selectedCategory = null
                            selectedProduct = null
                            quantityToDeliver = ""
                        },
                        label = { Text("Todos") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    // Category filter chips
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                                selectedProduct = null
                                quantityToDeliver = ""
                            },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                if (selectableProducts.isEmpty()) {
                    Text(
                        text = if (selectedCategory != null)
                            "Não há produtos disponíveis nesta categoria"
                        else
                            "Não há produtos disponíveis no inventário",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(
                        text = "Produtos disponíveis:",
                        style = MaterialTheme.typography.labelMedium
                    )

                    // List of available products
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(selectableProducts) { product ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedProduct = product },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selectedProduct?.id == product.id)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = product.name,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "Disponível: ${product.quantity} ${product.unit} • ${product.category}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Quantity input
                    if (selectedProduct != null) {
                        OutlinedTextField(
                            value = quantityToDeliver,
                            onValueChange = {
                                // Only allow numbers
                                if (it.isEmpty() || it.toIntOrNull() != null) {
                                    quantityToDeliver = it
                                }
                            },
                            label = { Text("Quantidade a entregar") },
                            modifier = Modifier.fillMaxWidth(),
                            supportingText = {
                                Text("Disponível: ${selectedProduct?.quantity} ${selectedProduct?.unit}")
                            },
                            isError = quantityToDeliver.toIntOrNull()?.let { qty ->
                                qty > (selectedProduct?.quantity ?: 0) || qty <= 0
                            } ?: false
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedProduct?.let { product ->
                        val qty = quantityToDeliver.toIntOrNull() ?: 0
                        if (qty > 0 && qty <= product.quantity) {
                            onAdd(
                                product.copy(
                                    id = UUID.randomUUID().toString(), // New ID for delivery item
                                    quantity = qty
                                )
                            )
                        }
                    }
                },
                enabled = selectedProduct != null &&
                    quantityToDeliver.toIntOrNull()?.let { qty ->
                        qty > 0 && qty <= (selectedProduct?.quantity ?: 0)
                    } == true
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
