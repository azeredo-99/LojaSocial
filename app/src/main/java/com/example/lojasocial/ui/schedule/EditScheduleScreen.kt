package com.example.lojasocial.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.models.Schedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleScreen(
    nav: NavController,
    schedule: Schedule,
    vm: ScheduleViewModel
) {
    var beneficiaryName by remember { mutableStateOf(schedule.beneficiaryName) }
    var date by remember { mutableStateOf(schedule.date) }
    var time by remember { mutableStateOf(schedule.time) }
    var notes by remember { mutableStateOf(schedule.notes) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Agendamento") },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = beneficiaryName,
                onValueChange = { beneficiaryName = it },
                label = { Text("Nome do beneficiário") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Data") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Hora") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Observações") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    vm.update(
                        schedule.copy(
                            beneficiaryName = beneficiaryName,
                            date = date,
                            time = time,
                            notes = notes
                        )
                    )
                    nav.popBackStack()
                }
            ) {
                Text("Guardar alterações")
            }
        }
    }
}
