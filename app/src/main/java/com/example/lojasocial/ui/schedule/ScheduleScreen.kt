package com.example.lojasocial.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    nav: NavController,
    vm: ScheduleViewModel = hiltViewModel()
) {
    var scheduleToDelete by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        vm.load()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendamentos") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { nav.navigate("addSchedule") }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar")
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

            vm.schedules.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum agendamento registado")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vm.schedules) { schedule ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column {
                                    Text(
                                        schedule.beneficiaryName,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text("Data: ${schedule.date}")
                                    Text("Hora: ${schedule.time}")
                                }

                                Row {
                                    IconButton(
                                        onClick = {
                                            nav.navigate("editSchedule/${schedule.id}")
                                        }
                                    ) {
                                        Icon(Icons.Default.Edit, "Editar")
                                    }

                                    IconButton(
                                        onClick = {
                                            scheduleToDelete = schedule.id
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            "Remover",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* -------- CONFIRMAÇÃO REMOVER -------- */
    if (scheduleToDelete != null) {
        AlertDialog(
            onDismissRequest = { scheduleToDelete = null },
            title = { Text("Remover agendamento") },
            text = { Text("Tens a certeza que queres remover este agendamento?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.remove(scheduleToDelete!!)
                        scheduleToDelete = null
                    }
                ) {
                    Text("Remover", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { scheduleToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
