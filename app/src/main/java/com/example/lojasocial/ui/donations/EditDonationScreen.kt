package com.example.lojasocial.ui.donations

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
import com.example.lojasocial.models.Donation
import com.example.lojasocial.models.Product
import com.example.lojasocial.repository.DonationRepository
import com.example.lojasocial.repository.ProductRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDonationScreen(
    donationId: String,
    nav: NavController,
    vm: DonationsViewModel = hiltViewModel()
) {
    var donorName by remember { mutableStateOf("") }
    var donorContact by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var donationProducts by remember { mutableStateOf<List<Product>>(emptyList()) }

    var availableProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoadingProducts by remember { mutableStateOf(true) }
    var isLoadingDonation by remember { mutableStateOf(true) }

    var showAddProductDialog by remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf(Calendar.getInstance().timeInMillis) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val formattedDate = dateFormatter.format(Date(selectedDate))

    val scope = rememberCoroutineScope()

    /* ---------- LOAD DONATION ---------- */
    LaunchedEffect(donationId) {
        scope.launch {
            try {
                isLoadingDonation = true
                val donation = DonationRepository.getById(donationId)

                donation?.let {
                    donorName = it.donorName
                    donorContact = it.donorContact
                    notes = it.notes
                    donationProducts = it.items

                    try {
                        val parsed = dateFormatter.parse(it.date)
                        if (parsed != null) selectedDate = parsed.time
                    } catch (_: Exception) {}
                }
            } finally {
                isLoadingDonation = false
            }
        }
    }

    /* ---------- LOAD INVENTORY ---------- */
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                availableProducts = ProductRepository.getAll()
            } finally {
                isLoadingProducts = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Doação") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->

        if (isLoadingDonation) {
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
                        .padding(bottom = 80.dp),
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    item {
                        OutlinedTextField(
                            value = donorName,
                            onValueChange = { donorName = it },
                            label = { Text("Nome do doador") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = donorContact,
                            onValueChange = { donorContact = it },
                            label = { Text("Contacto") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = formattedDate,
                            onValueChange = {},
                            label = { Text("Data da doação") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Default.CalendarToday, null)
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
                                "Produtos (${donationProducts.size})",
                                style = MaterialTheme.typography.titleMedium
                            )

                            IconButton(
                                onClick = { showAddProductDialog = true },
                                enabled = !isLoadingProducts
                            ) {
                                Icon(Icons.Default.Add, null)
                            }
                        }
                    }

                    if (donationProducts.isEmpty()) {
                        item {
                            Text(
                                "Nenhum produto adicionado",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(donationProducts) { product ->
                            DonationProductItem(
                                product = product,
                                onRemove = {
                                    donationProducts =
                                        donationProducts.filter { it.id != product.id }
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

                /* ---------- SAVE BUTTON ---------- */
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
                        enabled = donorName.isNotBlank() && donationProducts.isNotEmpty(),
                        onClick = {
                            vm.updateDonation(
                                Donation(
                                    id = donationId,
                                    donorName = donorName,
                                    donorContact = donorContact,
                                    date = formattedDate,
                                    items = donationProducts,
                                    notes = notes,
                                    received = true
                                )
                            )
                            nav.popBackStack()
                        },
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

    /* ---------- DATE PICKER ---------- */
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
                ) { Text("OK") }
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

    /* ---------- ADD PRODUCT DIALOG ---------- */
    if (showAddProductDialog) {
        SelectDonationProductDialog(
            availableProducts = availableProducts,
            alreadyAddedProducts = donationProducts,
            onDismiss = { showAddProductDialog = false },
            onAdd = { product ->
                donationProducts = donationProducts + product
                showAddProductDialog = false
            }
        )
    }
}
