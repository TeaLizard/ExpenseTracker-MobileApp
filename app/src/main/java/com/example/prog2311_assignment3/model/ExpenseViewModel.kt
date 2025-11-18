package com.example.prog2311_assignment3.model

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.prog2311_assignment3.data.Expense
import com.example.prog2311_assignment3.data.ExpenseDatabaseHelper

class ExpenseViewModel(application: Application): AndroidViewModel(application){
    private val dbHelper = ExpenseDatabaseHelper(application)

    var expenses by mutableStateOf(listOf<Expense>())
        private set
    var selectedExpense by mutableStateOf<Expense?>(null) // used for updating and deleting
        private set
    var snackMessage by mutableStateOf("")
        private set

    var title by mutableStateOf("")
    var amount by mutableStateOf<Int?>(null)
    var category by mutableStateOf("")
    val categories = listOf("Food", "Travel", "Bills")



    init {
        loadExpenses()
    }

    fun refreshState() {
        loadExpenses()
        resetInputFields()
        snackMessage = ""
        selectedExpense = null
    }
    private fun loadExpenses() {
        expenses = dbHelper.getExpenses()
    }
    private fun resetInputFields() {
        title = ""
        amount = null
        category = ""
    }

    private fun validateInput(): Boolean {
        if (title.isBlank()) {
            snackMessage = "Title cannot be empty"
            return false
        }
        if (amount == null) {
            snackMessage = "Amount cannot be empty"
            return false
        }
        if (category.isBlank()) {
            snackMessage = "Category cannot be empty"
            return false
        }
        if (amount!! < 0) {
            snackMessage = "Amount cannot be negative"
            return false
        }
        return true
    }

    fun selectExpenseToUpdate(expense: Expense) {
        selectedExpense = expense
        title = expense.title
        amount = expense.amount
        category = expense.category
    }

    fun selectExpenseToDelete(expense: Expense) {
        selectedExpense = expense
    }


    fun addExpense() {
        if (!validateInput()) { return }
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
            snackMessage = "Expense added"
        } else {
            snackMessage = "Error adding expense"
            return
        }
        resetInputFields()
    }

    fun updateExpense() {
        if (!validateInput()) { return }
        val updateId = selectedExpense?.id ?: 0
        if (updateId == 0) {
            snackMessage = "No expense selected"
            return
        }
        val expense = Expense (
            id = selectedExpense?.id ?: 0,
            title = title,
            amount = amount ?: 0,
            category = category
        )
        dbHelper.updateExpense(expense)
        val updatedExpense = dbHelper.getExpenseById(updateId)
        if (updatedExpense == null || updatedExpense != expense) {
            snackMessage = "Error updating expense"
            return
        }
        refreshState()
        snackMessage = "Expense updated"
    }

    fun deleteExpense() {
        val deleteId = selectedExpense?.id ?: 0
        if (deleteId == 0) {
            snackMessage = "No expense selected"
            return
        }
        dbHelper.deleteExpenseById(deleteId)
        if (dbHelper.getExpenseById(deleteId) != null) {
            snackMessage = "Error deleting expense"
            return
        }
        refreshState()
        snackMessage = "Expense deleted"
    }
}