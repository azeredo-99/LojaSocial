package com.example.lojasocial.ui.donations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.models.Donation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationsScreen(
    nav: NavController,
    vm: DonationsViewModel = hiltViewModel()
) {
    var donationToDelete by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) { vm.load() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Doações") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { nav.navigate("addDonation") }) {
                        Icon(Icons.Default.Add, contentDescription = "Nova doação")
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

            vm.donations.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhuma doação registada")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(vm.donations) { donation ->
                        DonationItem(
                            donation = donation,
                            onEdit = { nav.navigate("editDonation/${donation.id}") },
                            onDelete = { donationToDelete = donation.id },
                            onToggleReceived = { vm.toggleReceived(donation) }
                        )
                    }
                }
            }
        }
    }

    /* -------- CONFIRMAÇÃO REMOVER -------- */
    if (donationToDelete != null) {
        AlertDialog(
            onDismissRequest = { donationToDelete = null },
            title = { Text("Remover doação") },
            text = { Text("Confirmas a remoção desta doação?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.removeDonation(donationToDelete!!)
                        donationToDelete = null
                    }
                ) {
                    Text("Remover", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { donationToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun DonationItem(
    donation: Donation,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleReceived: () -> Unit
) {
    val receivedColor = Color(0xFF4CAF50)
    val pendingColor = MaterialTheme.colorScheme.secondary

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (donation.received)
                receivedColor.copy(alpha = 0.12f)
            else
                MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = donation.donorName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (donation.received) receivedColor else pendingColor
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (donation.received) Icons.Default.CheckCircle else Icons.Default.Pending,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = if (donation.received) "RECEBIDA" else "PENDENTE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }

            if (donation.donorContact.isNotBlank()) {
                Text(
                    text = "Contacto: ${donation.donorContact}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "Data: ${donation.date}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Produtos: ${donation.items.size}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (donation.notes.isNotBlank()) {
                Text(
                    text = donation.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onToggleReceived,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (donation.received) "Marcar pendente" else "Marcar recebida")
                }

                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Editar")
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
