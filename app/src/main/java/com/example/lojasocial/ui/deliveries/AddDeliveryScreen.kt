package com.example.lojasocial.ui.deliveries

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.models.Delivery
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
    var quantity by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(name, { name = it }, label = { Text("Nome do beneficiário") })
            OutlinedTextField(number, { number = it }, label = { Text("Nº estudante") })
            OutlinedTextField(course, { course = it }, label = { Text("Curso") })
            OutlinedTextField(quantity, { quantity = it }, label = { Text("Quantidade") })
            OutlinedTextField(notes, { notes = it }, label = { Text("Observações") })

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    vm.addDelivery(
                        Delivery(
                            beneficiaryName = name,
                            studentNumber = number,
                            course = course,
                            quantity = quantity.toIntOrNull() ?: 0,
                            notes = notes,
                            date = date
                        )
                    )
                    nav.popBackStack()
                }
            ) {
                Text("Guardar entrega")
            }
        }
    }
}
