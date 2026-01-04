package com.example.lojasocial.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.R
import com.example.lojasocial.repository.ResultWrapper

@Composable
fun LoginScreen(
    nav: NavController,
    vm: AuthViewModel
) {
    val uiState = vm.uiState


    LaunchedEffect(Unit) {
        val user = com.google.firebase.auth.FirebaseAuth
            .getInstance()
            .currentUser

        if (user != null) {
            nav.navigate("main") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // LOGO IPCA
        Image(
            painter = painterResource(id = R.drawable.ipca_logo),
            contentDescription = "IPCA",
            modifier = Modifier
                .height(120.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Loja Social",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        // EMAIL
        OutlinedTextField(
            value = vm.email,
            onValueChange = { vm.email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        // PASSWORD
        OutlinedTextField(
            value = vm.password,
            onValueChange = { vm.password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        // BOTÃO LOGIN
        Button(
            onClick = { vm.login() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(Modifier.height(12.dp))

        // LINKS
        TextButton(onClick = { nav.navigate("register") }) {
            Text("Criar conta")
        }

        TextButton(onClick = { nav.navigate("recover") }) {
            Text("Recuperar password")
        }

        Spacer(Modifier.height(24.dp))

        // ESTADOS
        when (uiState) {
            is ResultWrapper.Loading -> CircularProgressIndicator()

            is ResultWrapper.Error -> Text(
                text = uiState.exception.message ?: "Ocorreu um erro",
                color = MaterialTheme.colorScheme.error
            )

            is ResultWrapper.Success<*> -> {
                nav.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            }

            else -> {}
        }
    }
}
