package com.example.lojasocial.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lojasocial.R
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TopBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(vertical = 16.dp)
            .height(96.dp),
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            /**
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Loja Social",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 28.sp
            )
            **/
            Image(
                modifier = Modifier
                    //.padding(top = 20.dp)
                    .height(50.dp),
                painter = painterResource(id = R.drawable.loja_social_branco),
                contentDescription = "Loja Social Logo",
                alignment = Alignment.Center
            )
        }
    }
}
