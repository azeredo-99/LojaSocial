package com.example.lojasocial.ui.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

data class HomeDestination(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

val homeOptions = listOf(

    HomeDestination("Beneficiários", Icons.Filled.Person, "beneficiaries"),
    HomeDestination("Inventário", Icons.Filled.Inventory, "inventory"),
    HomeDestination("Entregas", Icons.Filled.LocalShipping, "deliveries"),

    HomeDestination("Doações", Icons.Filled.Favorite, "donations"),
    HomeDestination("Relatórios", Icons.Filled.BarChart, "reports"),
    HomeDestination("Alertas", Icons.Filled.Warning, "alerts"),
)
