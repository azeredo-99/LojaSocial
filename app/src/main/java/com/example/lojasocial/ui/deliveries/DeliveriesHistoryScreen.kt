package com.example.lojasocial.ui.deliveries

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.models.Delivery
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveriesHistoryScreen(
    nav: NavController,
    vm: DeliveriesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        vm.load()
    }

    // Sort deliveries by date (closest date first)
    val sortedDeliveries = remember(vm.deliveries) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        vm.deliveries.sortedBy { delivery ->
            try {
                dateFormat.parse(delivery.date)?.time ?: Long.MAX_VALUE
            } catch (e: Exception) {
                Long.MAX_VALUE
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico de Entregas") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { nav.navigate("addDelivery") }) {
                        Icon(Icons.Default.Add, contentDescription = "Nova entrega")
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

            vm.deliveries.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Não existem entregas registadas")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(sortedDeliveries) { delivery ->
                        DeliveryHistoryItem(
                            delivery = delivery,
                            onToggleState = { vm.toggleDeliveryState(delivery) },
                            onEdit = { nav.navigate("editDelivery/${delivery.id}") },
                            onDelete = { vm.removeDelivery(delivery.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeliveryHistoryItem(
    delivery: Delivery,
    onToggleState: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Calculate days until delivery
    val daysUntilDelivery = remember(delivery.date) {
        try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val deliveryDate = dateFormat.parse(delivery.date)
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            if (deliveryDate != null) {
                val diff = deliveryDate.time - today.time
                TimeUnit.MILLISECONDS.toDays(diff).toInt()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    val showWarning = !delivery.state && daysUntilDelivery != null && daysUntilDelivery in 0..2

    // Color schemes for delivered (green) and undelivered (neutral blue-gray)
    val deliveredContainerColor = Color(0xFF4CAF50).copy(alpha = 0.2f)
    val deliveredContentColor = Color(0xFF1B5E20)
    val deliveredBadgeColor = Color(0xFF4CAF50)
    val deliveredBadgeContentColor = Color.White

    val undeliveredContainerColor = Color(0xFF607D8B).copy(alpha = 0.2f)
    val undeliveredContentColor = Color(0xFF37474F)
    val undeliveredBadgeColor = Color(0xFF607D8B)
    val undeliveredBadgeContentColor = Color.White

    val deliveredButtonColor = Color(0xFF0D3D14)  // Darker green
    val undeliveredButtonColor = Color(0xFF1C2529)  // Darker blue-gray

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (delivery.state)
                deliveredContainerColor
            else
                undeliveredContainerColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Warning banner for upcoming deliveries
            if (showWarning) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    color = Color(0xFFFFA726).copy(alpha = 0.25f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFF57C00),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = when (daysUntilDelivery) {
                                0 -> "Entrega hoje!"
                                1 -> "Falta 1 dia"
                                else -> "Faltam $daysUntilDelivery dias"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFFF57C00)
                        )
                    }
                }
            }

            // Header with status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = delivery.beneficiaryName,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (delivery.state)
                        deliveredContentColor
                    else
                        undeliveredContentColor,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Delete icon button
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar entrega",
                            tint = if (delivery.state)
                                deliveredContentColor.copy(alpha = 0.7f)
                            else
                                undeliveredContentColor.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Status Badge
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (delivery.state)
                            deliveredBadgeColor
                        else
                            undeliveredBadgeColor
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (delivery.state) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (delivery.state)
                                    deliveredBadgeContentColor
                                else
                                    undeliveredBadgeContentColor
                            )
                            Text(
                                text = if (delivery.state) "ENTREGUE" else "POR ENTREGAR",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (delivery.state)
                                    deliveredBadgeContentColor
                                else
                                    undeliveredBadgeContentColor
                            )
                        }
                    }
                }
            }

            Text(
                "Nº estudante: ${delivery.studentNumber}",
                color = if (delivery.state)
                    deliveredContentColor
                else
                    undeliveredContentColor
            )
            Text(
                "Curso: ${delivery.course}",
                color = if (delivery.state)
                    deliveredContentColor
                else
                    undeliveredContentColor
            )

            HorizontalDivider(
                color = if (delivery.state)
                    deliveredContentColor.copy(alpha = 0.2f)
                else
                    undeliveredContentColor.copy(alpha = 0.2f)
            )

            Text(
                "Produtos: ${delivery.items.size}",
                color = if (delivery.state)
                    deliveredContentColor
                else
                    undeliveredContentColor
            )
            Text(
                "Data: ${delivery.date}",
                color = if (delivery.state)
                    deliveredContentColor
                else
                    undeliveredContentColor
            )

            if (delivery.notes.isNotBlank()) {
                HorizontalDivider(
                    color = if (delivery.state)
                        deliveredContentColor.copy(alpha = 0.2f)
                    else
                        undeliveredContentColor.copy(alpha = 0.2f)
                )
                Text(
                    delivery.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (delivery.state)
                        deliveredContentColor
                    else
                        undeliveredContentColor
                )
            }

            HorizontalDivider(
                color = if (delivery.state)
                    deliveredContentColor.copy(alpha = 0.2f)
                else
                    undeliveredContentColor.copy(alpha = 0.2f)
            )

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Toggle state button
                OutlinedButton(
                    onClick = onToggleState,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (delivery.state)
                            deliveredContentColor.copy(alpha = 0.10f)  // Background color
                        else
                            undeliveredContentColor.copy(alpha = 0.10f),  // Background color
                        contentColor = if (delivery.state)
                            deliveredButtonColor
                        else
                            undeliveredButtonColor
                    )
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (delivery.state) "Marcar como não entregue" else "Marcar como entregue",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Edit button
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (delivery.state)
                            deliveredContentColor.copy(alpha = 0.10f)  // Background color
                        else
                            undeliveredContentColor.copy(alpha = 0.10f),  // Background color
                        contentColor = if (delivery.state)
                            deliveredButtonColor
                        else
                            undeliveredButtonColor
                    )
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Editar",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }

    // Delete confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text("Eliminar entrega?")
            },
            text = {
                Text(
                    "Tem a certeza que deseja eliminar a entrega de ${delivery.beneficiaryName}? Esta ação não pode ser revertida."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
