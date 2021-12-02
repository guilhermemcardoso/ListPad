package me.gmcardoso.listpad.database.dao

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import me.gmcardoso.listpad.model.Category
import me.gmcardoso.listpad.model.Color

class ColorDAO(private var dbHelper: SQLiteOpenHelper) {

    companion object {
        private const val ID = "Id"
        private const val NAME = "Name"
        private const val HEX = "Hex"
        private const val TABLE_NAME = "Colors"
    }

    fun get(_id: Int): Color {
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM ${TABLE_NAME} WHERE ${ID} = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor?.moveToFirst()
        val list = Color(cursor.getInt(0),
            cursor.getString(1), cursor.getString(2))
        cursor.close()
        return list
    }

    fun colors(): ArrayList<Color> {
        val db = dbHelper.writableDatabase
        val colors = ArrayList<Color>()
        val selectQuery = "SELECT * FROM ${TABLE_NAME}"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val color = Color(cursor.getInt(0), cursor.getString(1), cursor.getString(2))
                    colors.add(color)
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        Log.d("COLORS", colors.toString())
        return colors
    }
}