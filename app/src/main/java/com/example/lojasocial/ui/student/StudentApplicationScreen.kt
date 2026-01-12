package com.example.lojasocial.ui.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.models.StudentApplication
import com.example.lojasocial.repository.ResultWrapper
import com.example.lojasocial.repository.StudentApplicationRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentApplicationScreen(nav: NavController) {
    val scope = rememberCoroutineScope()

    var nome by remember { mutableStateOf("") }
    var numeroAluno by remember { mutableStateOf("") }
    var curso by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telemovel by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }
    var erro by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Candidatura") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // NÃO usar popBackStack, porque "entry" foi removido do backstack
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = numeroAluno,
                onValueChange = { numeroAluno = it },
                label = { Text("Número de aluno") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = curso,
                onValueChange = { curso = it },
                label = { Text("Curso") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = telemovel,
                onValueChange = { telemovel = it },
                label = { Text("Telemóvel (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = mensagem,
                onValueChange = { mensagem = it },
                label = { Text("Mensagem (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            erro?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(6.dp))

            Button(
                onClick = {
                    erro = null

                    if (nome.isBlank() || numeroAluno.isBlank() || curso.isBlank() || email.isBlank()) {
                        erro = "Preenche os campos obrigatórios: nome, número de aluno, curso e email."
                        return@Button
                    }

                    loading = true

                    val app = StudentApplication(
                        nome = nome.trim(),
                        numeroAluno = numeroAluno.trim(),
                        curso = curso.trim(),
                        email = email.trim(),
                        telemovel = telemovel.trim(),
                        mensagem = mensagem.trim()
                    )

                    scope.launch {
                        when (val res = StudentApplicationRepository.submit(app)) {
                            is ResultWrapper.Success -> {
                                // Vai para ecrã de confirmação e remove a candidatura do backstack
                                nav.navigate("applicationSubmitted") {
                                    popUpTo("studentApplication") { inclusive = true }
                                }
                            }
                            is ResultWrapper.Error -> {
                                erro = res.exception.message ?: "Ocorreu um erro ao submeter a candidatura."
                            }
                            ResultWrapper.Loading -> Unit
                        }
                        loading = false
                    }
                },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.height(18.dp))
                } else {
                    Text("Submeter candidatura")
                }
            }
        }
    }
}
