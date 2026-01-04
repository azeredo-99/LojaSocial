package com.example.lojasocial.ui.beneficiaries

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.models.Beneficiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBeneficiaryScreen(
    nav: NavController,
    vm: BeneficiariesViewModel,
    beneficiary: Beneficiary
) {
    var name by remember { mutableStateOf(beneficiary.name) }
    var number by remember { mutableStateOf(beneficiary.studentNumber) }
    var course by remember { mutableStateOf(beneficiary.course) }
    var active by remember { mutableStateOf(beneficiary.active) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Beneficiário") },
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

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = active, onCheckedChange = { active = it })
                Spacer(Modifier.width(8.dp))
                Text(if (active) "Ativo" else "Inativo")
            }

            Button(
                onClick = {
                    vm.updateBeneficiary(
                        beneficiary.copy(
                            name = name,
                            studentNumber = number,
                            course = course,
                            active = active
                        )
                    )
                    nav.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar alterações")
            }
        }
    }
}
