package com.example.lojasocial.ui.help

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Ajuda", style = MaterialTheme.typography.headlineMedium)

        Text("Esta aplicação destina-se à gestão da Loja Social do IPCA.")

        Text("Em caso de dúvidas, contacte os Serviços de Ação Social.")

        Text("Email: sas@ipca.pt")
        Text("Telefone: 253 802 500")
    }
}
