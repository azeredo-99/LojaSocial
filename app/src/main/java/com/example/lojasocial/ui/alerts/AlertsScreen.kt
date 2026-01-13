package com.example.lojasocial.ui.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.models.Alert
import com.example.lojasocial.repository.ResultWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertsScreen(
    nav: NavController,
    vm: AlertsViewModel
) {
    val alertsState = vm.alerts.collectAsState()
    val statusState = vm.status.collectAsState()

    LaunchedEffect(Unit) { vm.loadActive() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Alertas") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { vm.generate() }) { Text("Atualizar") }
                //OutlinedButton(onClick = { vm.loadActive() }) { Text("Meow") }
            }

            Spacer(Modifier.height(12.dp))

            when (val st = statusState.value) {
                ResultWrapper.Loading -> {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(12.dp))
                }

                is ResultWrapper.Error -> {
                    Text(st.exception.message ?: "Erro")
                    Spacer(Modifier.height(12.dp))
                }

                is ResultWrapper.Success -> {

                }

                null -> Unit
            }

            if (alertsState.value.isEmpty()) {
                Text("Sem alertas ativos.")
                return@Column
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(alertsState.value) { alert ->
                    AlertCard(
                        alert = alert,
                        onResolve = { vm.resolve(alert.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AlertCard(
    alert: Alert,
    onResolve: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(alert.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(alert.message, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${alert.type} • ${alert.severity}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onResolve) { Text("Marcar como resolvido") }
            }
        }
    }
}
