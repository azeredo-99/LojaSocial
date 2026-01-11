package com.example.lojasocial.ui.donations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory
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
import com.example.lojasocial.repository.ProductRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDonationScreen(
    nav: NavController,
    vm: DonationsViewModel = hiltViewModel()
) {
    var donorName by remember { mutableStateOf("") }
    var donorContact by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Produtos recebidos
    var donationProducts by remember { mutableStateOf<List<Product>>(emptyList()) }

    // Produtos disponíveis (inventário)
    var availableProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoadingProducts by remember { mutableStateOf(true) }

    var showAddProductDialog by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val date = remember { dateFormatter.format(Date()) }

    val scope = rememberCoroutineScope()

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
                title = { Text("Nova Doação") },
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Produtos recebidos (${donationProducts.size})",
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
                        vm.addDonation(
                            Donation(
                                id = UUID.randomUUID().toString(),
                                donorName = donorName,
                                donorContact = donorContact,
                                date = date,
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
                    Text("Guardar doação")
                }
            }
        }
    }

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

/* ---------------- COMPONENTE ITEM ---------------- */

@Composable
fun DonationProductItem(
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Inventory, null)
                Column {
                    Text(product.name, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "${product.quantity} ${product.unit} • ${product.category}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
