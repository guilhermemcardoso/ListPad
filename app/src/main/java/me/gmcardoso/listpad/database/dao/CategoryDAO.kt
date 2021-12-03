package me.gmcardoso.listpad.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import me.gmcardoso.listpad.model.Category

class CategoryDAO(private var dbHelper: SQLiteOpenHelper) {

    companion object {
        private const val ID = "Id"
        private const val NAME = "Name"
        private const val DEFAULT = "IsDefault"
        private const val COLOR = "Color"
        private const val TABLE_NAME = "Categories"
    }

    fun save(category: Category): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(NAME, category.name)
        values.put(DEFAULT, 0)
        values.put(COLOR, category.color)
        val success = db.insert(TABLE_NAME,null,values)
        db.close()
        return ("$success").toInt() != -1
    }

    fun get(_id: Int): Category {
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM ${TABLE_NAME} WHERE ${ID} = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor?.moveToFirst()
        val list = Category(cursor.getInt(0),
            cursor.getString(1), cursor.getInt(2), cursor.getString(3))
        cursor.close()
        return list
    }

    fun categories(): ArrayList<Category> {
        val db = dbHelper.writableDatabase
        val categories = ArrayList<Category>()
        val selectQuery = "SELECT * FROM ${TABLE_NAME}"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val category = Category(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3))
                    categories.add(category)
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return categories
    }

    fun update(category: Category): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(NAME, category.name)
            put(COLOR, category.color)
        }
        val success = db.update(TABLE_NAME, values, "${ID}=?", arrayOf(category.id.toString())).toLong()
        db.close()
        return ("$success").toInt() != -1
    }

    fun delete(_id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val success = db.delete(TABLE_NAME, "${ID}=?", arrayOf(_id.toString())).toLong()
        return ("$success").toInt() != -1
    }
}