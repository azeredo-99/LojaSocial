package com.example.lojasocial.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    nav: NavController,
    vm: AuthViewModel,
    successRoute: String,
    title: String,
    showRegisterOption: Boolean,
    signOutOnBack: Boolean
) {
    val uiState = vm.uiState

    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            nav.navigate(successRoute) {
                popUpTo("login") { inclusive = true }
                popUpTo("loginStudent") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            vm.clearState()

                            if (signOutOnBack) {
                                FirebaseAuth.getInstance().signOut()
                                vm.email = ""
                                vm.password = ""
                                vm.name = ""
                            }

                            nav.navigate("entry") {
                                popUpTo("entry") { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

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

            OutlinedTextField(
                value = vm.email,
                onValueChange = { vm.email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = vm.password,
                onValueChange = { vm.password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { vm.login() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar")
            }

            Spacer(Modifier.height(12.dp))

            TextButton(
                onClick = {
                    vm.clearState()
                    nav.navigate("recover")
                }
            ) {
                Text("Recuperar password")
            }

            if (showRegisterOption) {
                TextButton(
                    onClick = {
                        vm.clearState()
                        nav.navigate("register")
                    }
                ) {
                    Text("Criar conta")
                }
            }

            Spacer(Modifier.height(24.dp))

            when (uiState) {
                is ResultWrapper.Loading -> CircularProgressIndicator()

                is ResultWrapper.Error -> Text(
                    text = uiState.exception.message ?: "Ocorreu um erro",
                    color = MaterialTheme.colorScheme.error
                )

                is ResultWrapper.Success<*> -> {
                    vm.clearState()
                    nav.navigate(successRoute) {
                        popUpTo("login") { inclusive = true }
                        popUpTo("loginStudent") { inclusive = true }
                    }
                }

                else -> Unit
            }
        }
    }
}
