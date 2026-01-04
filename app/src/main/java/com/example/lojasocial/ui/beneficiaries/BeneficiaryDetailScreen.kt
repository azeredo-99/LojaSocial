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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryDetailScreen(
    nav: NavController,
    vm: BeneficiariesViewModel,
    beneficiaryId: String
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.load()
    }

    val beneficiary = vm.beneficiaries.firstOrNull { it.id == beneficiaryId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhe do Beneficiário") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->

        if (beneficiary == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = beneficiary.name,
                    style = MaterialTheme.typography.headlineSmall
                )

                Divider()

                Text("Nº Estudante: ${beneficiary.studentNumber}")
                Text("Curso: ${beneficiary.course}")
                Text(
                    if (beneficiary.active) "Estado: Ativo" else "Estado: Inativo",
                    color = if (beneficiary.active)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        nav.navigate("editBeneficiary/${beneficiary.id}")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar beneficiário")
                }

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Remover beneficiário")
                }
            }
        }
    }

    // CONFIRMAÇÃO
    if (showDeleteDialog && beneficiary != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Remover beneficiário") },
            text = {
                Text(
                    "Tens a certeza que queres remover este beneficiário? " +
                            "Esta ação não pode ser desfeita."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.removeBeneficiary(beneficiary.id)
                        showDeleteDialog = false
                        nav.popBackStack() // volta à lista
                    }
                ) {
                    Text(
                        "Remover",
                        color = MaterialTheme.colorScheme.error
                    )
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
