package com.example.lojasocial.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.repository.ResultWrapper

@Composable
fun RegisterScreen(
    nav: NavController,
    vm: AuthViewModel
) {
    val uiState = vm.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Criar Conta", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(20.dp))

        TextField(
            value = vm.name,
            onValueChange = { vm.name = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        TextField(
            value = vm.email,
            onValueChange = { vm.email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        TextField(
            value = vm.password,
            onValueChange = { vm.password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { vm.register() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Criar conta")
        }

        Spacer(Modifier.height(10.dp))

        TextButton(onClick = { nav.popBackStack() }) {
            Text("Voltar")
        }

        Spacer(Modifier.height(20.dp))

        when (uiState) {
            is ResultWrapper.Loading -> CircularProgressIndicator()
            is ResultWrapper.Error -> Text("Erro: ${uiState.exception.message}")
            is ResultWrapper.Success -> {
                nav.navigate("home") {
                    popUpTo("register") { inclusive = true }
                }
            }
            else -> {}
        }
    }
}
