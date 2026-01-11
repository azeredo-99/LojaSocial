package com.example.lojasocial.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    rootNavController: NavController,
    vm: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) { vm.loadProfile() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Minha Conta") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /* ---------- HEADER PERFIL ---------- */
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = vm.name,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = FirebaseAuth.getInstance().currentUser?.email ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    AssistChip(
                        onClick = {},
                        label = { Text("Funcionário") }
                    )
                }
            }

            /* ---------- CONTA ---------- */
            Text("Conta", style = MaterialTheme.typography.titleSmall)

            Card {
                Column {
                    ListItem(
                        headlineContent = { Text("Editar perfil") },
                        leadingContent = { Icon(Icons.Default.Edit, null) },
                        modifier = Modifier.clickable {
                            rootNavController.navigate("editProfile")
                        }
                    )

                    Divider()

                    ListItem(
                        headlineContent = { Text("Alterar palavra-passe") },
                        leadingContent = { Icon(Icons.Default.Lock, null) },
                        modifier = Modifier.clickable {
                            rootNavController.navigate("changePassword")
                        }
                    )
                }
            }

            /* ---------- SESSÃO ---------- */
            Text("Sessão", style = MaterialTheme.typography.titleSmall)

            Card {
                ListItem(
                    headlineContent = {
                        Text(
                            "Terminar sessão",
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.Logout,
                            null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    modifier = Modifier.clickable {
                        FirebaseAuth.getInstance().signOut()
                        rootNavController.navigate("login") {
                            popUpTo(0)
                        }
                    }
                )
            }

            vm.message?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
