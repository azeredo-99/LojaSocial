package com.example.lojasocial.ui.deliveries

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
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
import com.example.lojasocial.repository.DeliveryRepository
import com.example.lojasocial.repository.ProductRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDeliveryScreen(
    deliveryId: String,
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
    var isLoadingDelivery by remember { mutableStateOf(true) }

    // Dialog para adicionar produto
    var showAddProductDialog by remember { mutableStateOf(false) }

    // Date picker state
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance().timeInMillis) }

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val formattedDate = dateFormatter.format(Date(selectedDate))

    val scope = rememberCoroutineScope()

    // Load existing delivery data
    LaunchedEffect(deliveryId) {
        scope.launch {
            try {
                isLoadingDelivery = true
                val delivery = DeliveryRepository.getById(deliveryId)
                if (delivery != null) {
                    number = delivery.studentNumber
                    name = delivery.beneficiaryName
                    course = delivery.course
                    notes = delivery.notes
                    deliveryProducts = delivery.items

                    // Parse date
                    try {
                        val date = dateFormatter.parse(delivery.date)
                        if (date != null) {
                            selectedDate = date.time
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoadingDelivery = false
            }
        }
    }

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
                title = { Text("Editar Entrega") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        if (isLoadingDelivery) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
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
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        } finally {
                                            isLoadingBeneficiary = false
                                        }
                                    }
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
                            vm.updateDelivery(
                                Delivery(
                                    id = deliveryId,
                                    beneficiaryName = name,
                                    studentNumber = number,
                                    course = course,
                                    date = formattedDate,
                                    state = false, // Keep state as is (will be preserved in update)
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
                        Text("Guardar alterações")
                    }
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
