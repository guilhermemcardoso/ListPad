package me.gmcardoso.listpad.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_TABLE: String

        CREATE_TABLE = "CREATE TABLE Lists (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Description TEXT, Urgent INTEGER, CategoryId INTEGER)"
        db?.execSQL(CREATE_TABLE)

        CREATE_TABLE =
            "CREATE TABLE Categories (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, IsDefault INTEGER, Color TEXT)"
        db?.execSQL(CREATE_TABLE)

        CREATE_TABLE =
            "CREATE TABLE Items (Id INTEGER PRIMARY KEY AUTOINCREMENT, Description TEXT, Completed INTEGER, ListId INTEGER)"
        db?.execSQL(CREATE_TABLE)

        CREATE_TABLE =
            "CREATE TABLE Colors (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Hex TEXT)"
        db?.execSQL(CREATE_TABLE)

        createDefaultCategories(db)
        createDefaultColors(db)
    }

    private fun createDefaultCategories(db: SQLiteDatabase?) {
        var values = ContentValues()
        values.put("Name", "Tarefas")
        values.put("IsDefault", 1)
        values.put("Color", "a5d6a7")
        db?.insert("Categories", null, values)
        values = ContentValues()
        values.put("Name", "Compras")
        values.put("IsDefault", 1)
        values.put("Color", "90caf9")
        db?.insert("Categories", null, values)
        values = ContentValues()
        values.put("Name", "Compromissos")
        values.put("IsDefault", 1)
        values.put("Color", "ef9a9a")
        db?.insert("Categories", null, values)
        values = ContentValues()
        values.put("Name", "Geral")
        values.put("IsDefault", 1)
        values.put("Color", "ffe082")
        db?.insert("Categories", null, values)
    }

    private fun createDefaultColors(db: SQLiteDatabase?) {
        var values = ContentValues()
        values.put("Name", "blue")
        values.put("Hex", "90caf9")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "red")
        values.put("Hex", "ef9a9a")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "pink")
        values.put("Hex", "f48fb1")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "purple")
        values.put("Hex", "ce93d8")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "deep_purple")
        values.put("Hex", "b39ddb")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "indigo")
        values.put("Hex", "9fa8da")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "light_blue")
        values.put("Hex", "81d4fa")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "cyan")
        values.put("Hex", "80deea")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "teal")
        values.put("Hex", "80cbc4")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "green")
        values.put("Hex", "a5d6a7")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "light_green")
        values.put("Hex", "c5e1a5")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "lime")
        values.put("Hex", "e6ee9c")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "yellow")
        values.put("Hex", "fff59d")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "amber")
        values.put("Hex", "ffe082")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "orange")
        values.put("Hex", "ffcc80")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "deep_orange")
        values.put("Hex", "ffab91")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "brown")
        values.put("Hex", "bcaaa4")
        db?.insert("Colors", null, values)

        values = ContentValues()
        values.put("Name", "blue_grey")
        values.put("Hex", "b0bec5")
        db?.insert("Colors", null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var DROP_TABLE = "DROP TABLE IF EXISTS ITEMS"
        db?.execSQL(DROP_TABLE)

        DROP_TABLE = "DROP TABLE IF EXISTS LISTS"
        db?.execSQL(DROP_TABLE)

        DROP_TABLE = "DROP TABLE IF EXISTS CATEGORIES"
        db?.execSQL(DROP_TABLE)

        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "ListPad"

    }
}