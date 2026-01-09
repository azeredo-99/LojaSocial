package com.example.lojasocial.ui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lojasocial.models.Product
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    nav: NavController,
    vm: ProductsViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedUnit by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    val categories = listOf(
        "Alimentação",
        "Bebidas",
        "Higiene",
        "Limpeza"
    )

    val units = listOf(
        "unidades",
        "kg",
        "g",
        "L",
        "ml",
        "caixas",
        "pacotes",
        "latas"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Novo Produto") },
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

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Nome do produto
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome do produto") },
                modifier = Modifier.fillMaxWidth()
            )

            // Dropdown de categorias
            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                category = cat
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            // Dropdown de unidades
            ExposedDropdownMenuBox(
                expanded = expandedUnit,
                onExpandedChange = { expandedUnit = !expandedUnit }
            ) {
                OutlinedTextField(
                    value = unit,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Unidade") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnit) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedUnit,
                    onDismissRequest = { expandedUnit = false }
                ) {
                    units.forEach { u ->
                        DropdownMenuItem(
                            text = { Text(u) },
                            onClick = {
                                unit = u
                                expandedUnit = false
                            }
                        )
                    }
                }
            }

            // Campo de quantidade
            OutlinedTextField(
                value = quantity,
                onValueChange = { newValue ->
                    // Permite apenas números
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        quantity = newValue
                    }
                },
                label = { Text("Quantidade") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            // Campo de validade
            OutlinedTextField(
                value = selectedDate?.let {
                    val calendar = Calendar.getInstance().apply { timeInMillis = it }
                    "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
                } ?: "",
                onValueChange = {},
                label = { Text("Data de validade (opcional)") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Row {
                        if (selectedDate != null) {
                            TextButton(onClick = { selectedDate = null }) {
                                Text("Limpar")
                            }
                        }
                        TextButton(onClick = { showDatePicker = true }) {
                            Text("Selecionar")
                        }
                    }
                }
            )

            // Botão Guardar
            Button(
                onClick = {
                    if (name.isNotBlank() &&
                        unit.isNotBlank() &&
                        category.isNotBlank() &&
                        quantity.isNotBlank()
                    ) {
                        vm.addProduct(
                            Product(
                                name = name,
                                unit = unit,
                                category = category,
                                quantity = quantity.toIntOrNull() ?: 0,
                                expireDate = selectedDate
                            )
                        )
                        nav.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank() &&
                        unit.isNotBlank() &&
                        category.isNotBlank() &&
                        quantity.isNotBlank()
            ) {
                Text("Guardar produto")
            }
        }

        // DatePicker Dialog
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedDate = datePickerState.selectedDateMillis
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}