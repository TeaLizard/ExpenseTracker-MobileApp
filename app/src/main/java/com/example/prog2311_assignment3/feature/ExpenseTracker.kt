package com.example.prog2311_assignment3.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prog2311_assignment3.R
import com.example.prog2311_assignment3.composable.CategoryRadioButtonContainer
import com.example.prog2311_assignment3.composable.ExpenseCard
import com.example.prog2311_assignment3.model.ExpenseViewModel

@Composable
fun ExpenseTracker(modifier: Modifier = Modifier) {
    val viewModel: ExpenseViewModel = viewModel()
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val coroutineScope = rememberCoroutineScope()

    Column{
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.app_title) , color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.headlineLarge)
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(stringResource(R.string.expense_title), style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = { Text("") },
                modifier = Modifier.fillMaxWidth()
            )
            Text(stringResource(R.string.expense_amount), style = MaterialTheme.typography.bodyLarge)
            OutlinedTextField(
                value = viewModel.amount?.toString() ?: "",
                onValueChange = { input ->
                    val digitsOnlyString = input.filter { it.isDigit() }
                    val digitsAsInt = digitsOnlyString.toIntOrNull()
                    if (digitsAsInt != null) {
                        viewModel.amount = digitsAsInt
                    } else if (digitsOnlyString.isEmpty()) {
                        viewModel.amount = null
                    }
                },
                label = { Text("") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(stringResource(R.string.expense_category), style = MaterialTheme.typography.bodyLarge)
            CategoryRadioButtonContainer(viewModel)

            if (viewModel.selectedExpense == null) {
                Button(
                    onClick = { viewModel.addExpense() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.save_button))
                }
            } else {
                Button(
                    onClick = { viewModel.updateExpense() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.update_button))
                }
            }

            if (viewModel.selectedExpense != null) {
                Button(
                    onClick = { viewModel.refreshState() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }

            SnackbarHost(snackbarHostState)
            LaunchedEffect(viewModel.snackMessage) {
                if (viewModel.snackMessage.isNotEmpty()) {
                    snackbarHostState.showSnackbar(
                        message = viewModel.snackMessage,
                        duration = SnackbarDuration.Short
                    )
                }
            }



            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(viewModel.expenses) { expense ->
                    ExpenseCard(expense, viewModel)
                }
            }
        }
    }
}