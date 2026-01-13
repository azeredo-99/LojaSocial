package com.example.lojasocial.ui.beneficiaries

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.models.StudentApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiariesScreen(
    nav: NavController,
    vm: BeneficiariesViewModel = hiltViewModel()
) {
    var search by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.load()
        vm.loadPendingApplications()
    }

    val filtered = vm.beneficiaries.filter {
        it.name.contains(search, ignoreCase = true) ||
                it.studentNumber.contains(search, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beneficiários") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { nav.navigate("addBeneficiary") }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Adicionar beneficiário"
                        )
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

            vm.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(
                        text = vm.errorMessage ?: "Ocorreu um erro",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {
                        OutlinedTextField(
                            value = search,
                            onValueChange = { search = it },
                            label = { Text("Pesquisar") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    if (vm.pendingApplications.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Candidaturas Pendentes",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        items(
                            items = vm.pendingApplications,
                            key = { it.id }
                        ) { app ->
                            StudentApplicationItem(
                                app = app,
                                onAccept = { vm.acceptApplication(app) },
                                onReject = { vm.rejectApplication(app.id) }
                            )
                        }

                        item {
                            Spacer(Modifier.height(4.dp))
                            HorizontalDivider()
                            Spacer(Modifier.height(4.dp))
                        }
                    }

                    if (filtered.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Nenhum beneficiário encontrado")
                            }
                        }
                    } else {
                        items(
                            items = filtered,
                            key = { it.id }
                        ) { beneficiary ->
                            BeneficiaryItem(
                                beneficiary = beneficiary,
                                onClick = { nav.navigate("beneficiaryDetail/${beneficiary.id}") }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentApplicationItem(
    app: StudentApplication,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(app.nome, style = MaterialTheme.typography.titleMedium)
            Text("N.º aluno: ${app.numeroAluno}")
            Text("Curso: ${app.curso}")
            Text("Email: ${app.email}")

            if (app.telemovel.isNotBlank()) Text("Telemóvel: ${app.telemovel}")
            if (app.mensagem.isNotBlank()) Text("Mensagem: ${app.mensagem}")

            Spacer(Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onAccept) { Text("Aceitar") }
                OutlinedButton(onClick = onReject) { Text("Rejeitar") }
            }
        }
    }
}
