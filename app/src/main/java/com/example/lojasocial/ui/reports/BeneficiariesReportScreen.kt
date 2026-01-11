package com.example.lojasocial.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.ui.beneficiaries.BeneficiariesViewModel
import com.example.lojasocial.ui.deliveries.DeliveriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiariesReportScreen(
    nav: NavController,
    beneficiariesVM: BeneficiariesViewModel = hiltViewModel(),
    deliveriesVM: DeliveriesViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        beneficiariesVM.load()
        deliveriesVM.load()
    }

    val totalBeneficiaries = beneficiariesVM.beneficiaries.size
    val totalDeliveries = deliveriesVM.deliveries.size

    val beneficiariesWithDeliveries = beneficiariesVM.beneficiaries.count { beneficiary ->
        deliveriesVM.deliveries.any {
            it.studentNumber == beneficiary.studentNumber
        }
    }

    val beneficiariesWithoutDeliveries =
        totalBeneficiaries - beneficiariesWithDeliveries

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Relatório de Beneficiários") },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (beneficiariesVM.isLoading || deliveriesVM.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ReportStatCard(
                    title = "Total de Beneficiários",
                    value = totalBeneficiaries.toString(),
                    icon = Icons.Default.Group
                )

                ReportStatCard(
                    title = "Beneficiários com Entregas",
                    value = beneficiariesWithDeliveries.toString(),
                    icon = Icons.Default.LocalShipping
                )

                ReportStatCard(
                    title = "Beneficiários sem Entregas",
                    value = beneficiariesWithoutDeliveries.toString(),
                    icon = Icons.Default.PersonOff
                )

                ReportStatCard(
                    title = "Total de Entregas",
                    value = totalDeliveries.toString(),
                    icon = Icons.Default.LocalShipping
                )
            }
        }
    }
}
