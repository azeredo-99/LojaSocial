package com.example.lojasocial.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object Profile : BottomNavItem("profile", "Minha Conta", Icons.Filled.AccountCircle)
    object Help : BottomNavItem("help", "Ajuda", Icons.Filled.Help)
}
