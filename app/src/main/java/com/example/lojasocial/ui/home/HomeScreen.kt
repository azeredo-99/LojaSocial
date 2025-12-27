package com.example.lojasocial.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    nav: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Loja Social") },
                actions = {
                    TextButton(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            nav.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { padding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(homeOptions) { option ->
                HomeGridItem(
                    item = option,
                    onClick = { nav.navigate(option.route) }
                )
            }
        }
    }
}
