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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiariesScreen(
    nav: NavController,
    vm: BeneficiariesViewModel = hiltViewModel()
) {
    var search by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.load()
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

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            /* Pesquisa */
            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                label = { Text("Pesquisar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            /* 🔄 LOADING */
            if (vm.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@Column
            }

            /* ERRO */
            vm.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                return@Column
            }

            /* VAZIO */
            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum beneficiário encontrado")
                }
                return@Column
            }

            /* LISTA */
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered) { beneficiary ->
                    BeneficiaryItem(
                        beneficiary = beneficiary,
                        onClick = {
                            nav.navigate("beneficiaryDetail/${beneficiary.id}")
                        }
                    )
                }
            }
        }
    }
}
