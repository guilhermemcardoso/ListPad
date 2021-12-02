package me.gmcardoso.listpad.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import me.gmcardoso.listpad.model.List

class ListDAO(private var dbHelper: SQLiteOpenHelper) {

    companion object {
        private const val TABLE_NAME = "Lists"
        private const val ID = "Id"
        private const val NAME = "Name"
        private const val DESCRIPTION = "Description"
        private const val URGENT = "Urgent"
        private const val CATEGORY_ID = "CategoryId"
    }

    fun save(list: List): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(NAME, list.name)
        values.put(DESCRIPTION, list.description)
        values.put(URGENT, list.urgent)
        values.put(CATEGORY_ID, list.categoryId)
        val success = db.insert(TABLE_NAME,null,values)
        db.close()
        return success
    }

    fun get(_id: Int): List {
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor?.moveToFirst()
        val list = List(cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getInt(3),
            cursor.getInt(4))
        cursor.close()
        return list
    }

    fun getByName(_listName: String): ArrayList<List> {
        val listOfLists = ArrayList<List>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $NAME = '$_listName'"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val list = List(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4))
                    listOfLists.add(list)
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return listOfLists
    }

    fun getByCategory(_categoryId: Int): ArrayList<List> {
        val listOfLists = ArrayList<List>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $CATEGORY_ID = $_categoryId"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val list = List(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4))
                    listOfLists.add(list)
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return listOfLists
    }

    fun lists(): ArrayList<List> {
        val listOfLists = ArrayList<List>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $URGENT DESC"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    val list = List(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4))
                    listOfLists.add(list)
                }while(cursor.moveToNext())
            }
        }
        cursor.close()
        return listOfLists
    }

    fun update(list: List): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(NAME, list.name)
            put(DESCRIPTION, list.description)
            put(URGENT, list.urgent)
            put(CATEGORY_ID, list.categoryId)
        }
        val success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(list.id.toString())).toLong()
        db.close()
        return ("$success").toInt() != -1
    }

    fun delete(_id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val success = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString())).toLong()
        return ("$success").toInt() != -1
    }
}