package com.example.prog2311_assignment3.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ExpenseDatabaseHelper(context: Context)
    : SQLiteOpenHelper(context, "expenses", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                amount INTEGER,
                category TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {
        db?.execSQL("DROP TABLE IF EXISTS expenses")
        onCreate(db)
    }

    fun addExpense(expense: Expense) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", expense.title)
            put("amount", expense.amount)
            put("category", expense.category)
        }
        db.insert("expenses", null, values)
        db.close()
    }

    fun getExpenses(): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM expenses", null)
        while (cursor.moveToNext()) {
            expenses.add(
                Expense(
                    // get the values from the cursor using string name, as it is safer than using hardcoded integers
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("amount")),
                    cursor.getString(cursor.getColumnIndexOrThrow("category"))
                )
            )
        }
        cursor.close()
        db.close()
        return expenses
    }

    fun getExpenseById(id: Int): Expense? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM expenses WHERE id = ?", arrayOf(id.toString()))
        // if there is a record at that id, return it, else return null
        val note = if (cursor.moveToFirst()) {
            Expense(
                cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("title")),
                cursor.getInt(cursor.getColumnIndexOrThrow("amount")),
                cursor.getString(cursor.getColumnIndexOrThrow("category"))
            )
        } else null
        cursor.close()
        db.close()
        return note
    }

    fun updateExpense(expense: Expense) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", expense.title)
            put("amount", expense.amount)
            put("category", expense.category)
        }
        db.update("expenses", values, "id = ?", arrayOf(expense.id.toString()))
        db.close()
    }

    fun deleteExpenseById(id: Int) {
        val db = writableDatabase
        db.delete("expenses", "id = ?", arrayOf(id.toString()))
        db.close()
    }
}
