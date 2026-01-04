package com.example.lojasocial.ui.beneficiaries

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.models.Beneficiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBeneficiaryScreen(
    nav: NavController,
    vm: BeneficiariesViewModel
) {
    var name by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo Beneficiário") },
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

            OutlinedTextField(name, { name = it }, label = { Text("Nome") })
            OutlinedTextField(number, { number = it }, label = { Text("Nº Estudante") })
            OutlinedTextField(course, { course = it }, label = { Text("Curso") })

            Button(
                onClick = {
                    vm.addBeneficiary(
                        Beneficiary(
                            name = name,
                            studentNumber = number,
                            course = course,
                            active = true
                        )
                    )
                    nav.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
