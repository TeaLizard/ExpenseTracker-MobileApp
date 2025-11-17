package com.example.prog2311_assignment3.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.prog2311_assignment3.data.Expense
import com.example.prog2311_assignment3.data.ExpenseDatabaseHelper

class ExpenseViewModel(application: Application): AndroidViewModel(application){
    private val dbHelper = ExpenseDatabaseHelper(application)

    var expenses by mutableStateOf(listOf<Expense>())
        private set
    var selectedExpense by mutableStateOf<Expense?>(null) // used for updating and deleting
        private set
    var errorMessage by mutableStateOf("")
        private set

    var id by mutableStateOf<Int?>(null)
    var title by mutableStateOf("")
    var amount by mutableStateOf<Int?>(null)
    var category by mutableStateOf("")


    init {
        loadExpenses()
    }

    private fun reloadPage() {
        loadExpenses()
        resetInputFields()
        errorMessage = ""
    }
    private fun loadExpenses() {
        expenses = dbHelper.getExpenses()
    }
    private fun resetInputFields() {
        id = null
        title = ""
        amount = null
        category = ""
    }

    fun selectExpense(expense: Expense) {
        selectedExpense = expense
    }

    fun addExpense() {
        val prevExpensesCount = expenses.size
        dbHelper.addExpense(
            Expense(
                id = 0,
                // id not used when adding
                title = title,
                amount = amount ?: 0,
                category = category
            )
        )
        loadExpenses()
        if (expenses.size > prevExpensesCount) { // bigger number of expenses means an expense was added
            errorMessage = ""
        } else {
            errorMessage = "Error adding expense"
            return
        }
        resetInputFields()
    }

    fun updateExpense() {
        val updateId = selectedExpense?.id ?: 0
        if (updateId == 0) {
            errorMessage = "No expense selected"
            return
        }
        val expense = Expense (
            id = selectedExpense?.id ?: 0,
            title = title,
            amount = amount ?: 0,
            category = category
        )
        dbHelper.updateExpense(expense)
        if (dbHelper.getExpenseById(updateId) == null || dbHelper.getExpenseById(updateId) != expense) {
            errorMessage = "Error updating expense"
            return
        }
        reloadPage()
    }

    fun deleteExpense() {
        val deleteId = selectedExpense?.id ?: 0
        if (deleteId == 0) {
            errorMessage = "No expense selected"
            return
        }
        dbHelper.deleteExpenseById(deleteId)
        if (dbHelper.getExpenseById(deleteId) != null) {
            errorMessage = "Error deleting expense"
            return
        }
        reloadPage()
    }
}