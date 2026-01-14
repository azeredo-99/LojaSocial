package com.example.lojasocial.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lojasocial.models.StudentApplication
import com.example.lojasocial.repository.ResultWrapper
import com.example.lojasocial.repository.StudentApplicationRepository
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentStatusScreen(nav: NavController) {

    var loading by remember { mutableStateOf(true) }
    var erro by remember { mutableStateOf<String?>(null) }
    var app by remember { mutableStateOf<StudentApplication?>(null) }

    fun terminarSessao() {
        FirebaseAuth.getInstance().signOut()
        nav.navigate("entry") {
            popUpTo("entry") { inclusive = true }
            launchSingleTop = true
        }
    }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid.isNullOrBlank()) {
            terminarSessao()
            return@LaunchedEffect
        }

        when (val res = StudentApplicationRepository.getMyApplication(uid)) {
            is ResultWrapper.Success<*> -> {
                @Suppress("UNCHECKED_CAST")
                app = res.data as StudentApplication?
            }
            is ResultWrapper.Error -> erro = res.exception.message ?: "Erro ao carregar o estado."
            else -> Unit
        }
        loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estado da candidatura") },
                actions = {
                    TextButton(onClick = { terminarSessao() }) {
                        Text("Terminar sessão")
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            when {
                loading -> CircularProgressIndicator()

                erro != null -> Text(
                    text = erro!!,
                    color = MaterialTheme.colorScheme.error
                )

                app == null -> Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Ainda não existe nenhuma candidatura submetida.",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(
                            onClick = { nav.navigate("studentApplication") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Criar candidatura")
                        }
                    }
                }

                else -> {
                    val estado = app!!.estado.uppercase()

                    val (titulo, descricao) = when (estado) {
                        "ACEITE" -> "Candidatura aceite" to "A tua candidatura foi aprovada. Serás contactado em breve."
                        "REJEITADA" -> "Candidatura rejeitada" to "A tua candidatura não foi aprovada. Podes submeter uma nova candidatura."
                        else -> "Candidatura pendente" to "A tua candidatura está a ser analisada. Volta a consultar mais tarde."
                    }

                    val badgeBg = when (estado) {
                        "ACEITE" -> Color(0xFFDFF5E1)     // verde claro
                        "REJEITADA" -> Color(0xFFFCE2E2)  // vermelho claro
                        else -> Color(0xFFFFF3D6)         // amarelo claro
                    }

                    val badgeText = when (estado) {
                        "ACEITE" -> Color(0xFF1B5E20)     // verde escuro
                        "REJEITADA" -> Color(0xFFB71C1C)  // vermelho escuro
                        else -> Color(0xFF8A6D00)         // amarelo escuro
                    }

                    val cardBg = when (estado) {
                        "ACEITE" -> Color(0xFFEAF7EC)     // verde muito suave
                        "REJEITADA" -> Color(0xFFFFEAEA)  // vermelho muito suave
                        else -> Color(0xFFFFF7E5)         // amarelo muito suave
                    }

                    val textoBadge = when (estado) {
                        "ACEITE" -> "ACEITE"
                        "REJEITADA" -> "REJEITADA"
                        else -> "PENDENTE"
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = cardBg)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                Surface(
                                    color = badgeBg,
                                    shape = MaterialTheme.shapes.large
                                ) {
                                    Text(
                                        text = textoBadge,
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        ),
                                        color = badgeText,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }

                                Text(
                                    text = titulo,
                                    style = MaterialTheme.typography.headlineSmall
                                )

                                Text(
                                    text = descricao,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }


                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = cardBg)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Dados submetidos",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text("Nome: ${app!!.nome}")
                                Text("N.º aluno: ${app!!.numeroAluno}")
                                Text("Curso: ${app!!.curso}")
                                Text("Email: ${app!!.email}")
                            }
                        }

                        if (estado == "REJEITADA") {
                            Button(
                                onClick = { nav.navigate("studentApplication") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Submeter nova candidatura")
                            }
                        }
                    }
                }
            }
        }
    }
}
