package com.example.lojasocial.ui.beneficiaries

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.lojasocial.models.Beneficiary

@Composable
fun BeneficiaryItem(
    beneficiary: Beneficiary,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(beneficiary.name)
        },
        supportingContent = {
            Text(
                "${beneficiary.studentNumber} · ${beneficiary.course}"
            )
        },
        leadingContent = {
            Icon(Icons.Default.Person, contentDescription = null)
        },
        trailingContent = {
            Text(
                if (beneficiary.active) "Ativo" else "Inativo",
                color = if (beneficiary.active)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )
        },
        modifier = Modifier.clickable { onClick() }
    )
}
